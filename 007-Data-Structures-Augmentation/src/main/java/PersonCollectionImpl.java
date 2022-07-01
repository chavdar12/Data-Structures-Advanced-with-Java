import java.util.*;
import java.util.stream.Collectors;

public class PersonCollectionImpl implements PersonCollection {

    private Map<String, Person> peopleByEmails;
    private Map<String, TreeSet<Person>> peopleByDomains;
    private Map<String, TreeSet<Person>> peopleByNameAndTown;
    private Set<Person> people;

    public PersonCollectionImpl() {
        this.peopleByEmails = new HashMap<>();
        this.peopleByDomains = new HashMap<>();
        this.peopleByNameAndTown = new HashMap<>();
        this.people = new TreeSet<>(Comparator.comparingInt(Person::getAge).thenComparing(Person::getEmail));
    }

    @Override
    public boolean add(String email, String name, int age, String town) {
        if (this.peopleByEmails.containsKey(email)) {

            return false;
        }

        Person person = new Person(email, name, age, town);
        this.people.add(person);
        this.peopleByEmails.putIfAbsent(email, person);

        String nameAndTown = name + town;
        this.peopleByNameAndTown.putIfAbsent(nameAndTown, new TreeSet<>(Person::compareTo));
        this.peopleByNameAndTown.get(nameAndTown).add(person);

        String domain = getDomain(email);
        this.peopleByDomains.putIfAbsent(domain, new TreeSet<>(Person::compareTo));
        this.peopleByDomains.get(domain).add(person);

        return true;
    }


    @Override
    public int getCount() {
        return this.peopleByEmails.size();
    }

    @Override
    public boolean delete(String email) {
        Person person = this.peopleByEmails.remove(email);

        if (person == null) {
            return false;
        }

        this.people.remove(person);
        this.peopleByDomains.get(getDomain(email)).remove(person);
        this.peopleByNameAndTown.get(person.getName() + person.getTown()).remove(person);

        return true;
    }

    @Override
    public Person find(String email) {
        return this.peopleByEmails.get(email);
    }

    @Override
    public Iterable<Person> findAll(String emailDomain) {
        TreeSet<Person> people = this.peopleByDomains.get(emailDomain);
        return people != null ? people : new TreeSet<>();
    }

    @Override
    public Iterable<Person> findAll(String name, String town) {
        TreeSet<Person> people = this.peopleByNameAndTown.get(name + town);
        return people != null ? people : new TreeSet<>();
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge) {
        return people.stream()
                .filter(person -> person.getAge() >= startAge
                        && person.getAge() <= endAge)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge, String town) {
        return people
                .stream()
                .filter(person -> person.getAge() >= startAge
                        && person.getAge() <= endAge
                        && person.getTown().equals(town))
                .collect(Collectors.toList());
    }

    private String getDomain(String email) {
        return email.substring(email.indexOf("@") + 1);
    }
}
