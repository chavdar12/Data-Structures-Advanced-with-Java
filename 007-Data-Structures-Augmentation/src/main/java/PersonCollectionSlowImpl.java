import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PersonCollectionSlowImpl implements PersonCollection {

    private Map<String, Person> people = new HashMap<>();

    @Override
    public boolean add(String email, String name, int age, String town) {
        if (!people.containsKey(email)) {
            people.put(email, new Person(email, name, age, town));
            return true;
        }

        return false;
    }

    @Override
    public int getCount() {
        return people.size();
    }

    @Override
    public boolean delete(String email) {
        if (people.containsKey(email)) {
            people.remove(email);

            return true;
        }

        return false;
    }

    @Override
    public Person find(String email) {
        if (people.containsKey(email)) {
            return people.get(email);
        }

        return null;
    }

    @Override
    public Iterable<Person> findAll(String emailDomain) {
        return people.values()
                .stream()
                .filter(person -> person.getEmail().endsWith("@" + emailDomain))
                .sorted(Comparator.comparing(Person::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Person> findAll(String name, String town) {
        return people.values()
                .stream()
                .filter(person -> person.getName().equals(name) && person.getTown().equals(town))
                .sorted(Comparator.comparing(Person::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge) {

        return people.values()
                .stream()
                .filter(person -> person.getAge() >= startAge || person.getAge() <= endAge)
                .sorted(Comparator.comparing(Person::getAge).thenComparing(Person::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge, String town) {
        return people.values()
                .stream()
                .filter(person -> (person.getAge() >= startAge || person.getAge() <= endAge) && person.getTown().equals(town))
                .sorted(Comparator.comparing(Person::getAge).thenComparing(Person::getEmail))
                .collect(Collectors.toList());
    }
}
