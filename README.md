# getIntoMockitoForUnitTesting
This repository is a small light weight reference to get into Mockito framework for unit testing.
# Intoduction 
## Course Notes
- There are just the notes, there will be a detailed tutorial reference after. 
#### Why unit testing? 
- Make sure at least 70% of your code has unit testing.
- `Clear for take off`: Do not push your code to repository without running the unit tests and correct possible errors.
- Prevents Bugs.
- Helps you think about your code.
- Fun

#### What is Unit Testing 



#### What is a unit of source code
A unit of source code is the smallest part of a code that can be tested. In Java languages, this is usually a method or a class.

#### What is unit testing
- Unit testing is the process through which units of source code are tested to verify if they work properly. 
- Performing unit tests is a way to ensure that all functionalities of an application are working as they should. 
- Unit tests inform the developer when a change in one unit interferes with the functionality of another. 
- Modern unit testing frameworks are typically implemented using the same code used by the system under test. 
- This enables a developer who is writing application code in Java to write their unit tests in Java as well.

#### What is mocking
- Mocking is a process used in unit testing when the unit being tested has external dependencies. 
- The **purpose of mocking** is __to isolate and focus on the code being tested and not on the behavior or state of external dependencies__. 
- In `mocking`, the dependencies are replaced by closely controlled replacements objects that simulate the behavior of the real ones. 
- There are three main possible types of replacement objects - `fakes`, `stubs` and `mocks`.

**`Fakes`**: 
- A Fake is an object that will replace the actual code by implementing the same interface but without interacting with other objects. 
- Usually the Fake is hard-coded to return fixed results. 
- To test for different use cases, a lot of Fakes must be introduced. 
- The problem :x: introduced by using Fakes is that when an interface has been modified, all fakes implementing this interface should be modified as well.

**`Stubs`**: 
- A Stub is an object that will return a specific result based on a specific set of inputs and usually won’t respond to anything outside of what is programed for the test. 


**`Mocks`**: 
- A Mock is a much more sophisticated version of a Stub. It will still return values like a Stub, but it can also be programmed with expectations in terms of how many times each method should be called, in which order and with what data. 

- More info [here](https://www.telerik.com/products/mocking/unit-testing.aspx#:~:text=The%20purpose%20of%20mocking%20is,behavior%20of%20the%20real%20ones.) and [here](https://www.j-labs.pl/en/tech-blog/mocks-stubs-and-spies-in-unit-testing-based-on-mockito/#:~:text=A%20stub%20is%20an%20object,output%2C%20independently%20from%20the%20input.).


#### Triple A (AAA) 
- Called also (BDD): Behaviour Driven Development.
- **AAA**: Arrange, Act, Assert: A common pattern for writing tests where you set up the test data, perform the action, and then verify the results.
1. **Arrange**: Prepare instances
2. **Act**: Do the action to be tested
3. **Assert**: Test the results 

![AAA](./imgs/AAA.PNG)

#### Testing our project 

- In our case we will cover unit testing in 3 layers:
    * Repository layer 
    * Service layer 
    * Controller layer 

##### Test the Repository

- As contxt We have the entity Person and it's Repository PersonRepository and we want to test it.

![repo](./imgs/repo.PNG)

- Here the code.
Person.java
```java
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Person {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;
    String firstName;
    String lastName;
    @Temporal(TemporalType.DATE)
    Date birthDate;
}
```
PersonRepository.java
```java
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}

```
- __Lets test this Repository__
- The first step is to **Add the needed dependencies :**
pom.xml
```xml
<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<!-- https://mvnrepository.com/artifact/junit/junit -->
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>4.13.2</version>
			<scope>test</scope>
		</dependency>

	</dependencies>
```
- As shown we set the scope test for `h2`, `spring-boot-starter-test` and `junit` dependencies.
- H2 is used only for testing Here.
- We test here the repository so we are using only `spring-boot-starter-data-jpa`.
- **Create testing packages**
- Now we should create the testing class for the Repository, we will name it `PersonRepositoryTest`.
- The test class should be in the same packaging level of the tested class related to it (PersonRepository) in our case.

![repo_test](./imgs/repo_test.PNG)

- Here is the code of our test class `PersonRepositoryTest`.

```java
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@RunWith(SpringRunner.class) // Resolved the issue of NullPointerException in personRepository
public class PersonRepositoryTest {
    @Autowired
    private PersonRepository personRepository;

    @Test
    public void PersonRepository_saveAll_returnSavedPerson(){
        // Arrange
        Person person = Person.builder()
                .firstName("Essadeq")
                .lastName("EL AAMIRI")
                .birthDate(new Date(Date.valueOf("1999-01-07").getTime()))
                .build();

        // Act
        Person savedPerson = personRepository.save(person);

        // Assert
        Assertions.assertThat(savedPerson).isNotNull();
        Assertions.assertThat(savedPerson.getId()).isPositive();
        Assertions.assertThat(savedPerson.getFirstName()).isEqualTo(person.getFirstName());

    }

}
```
- Let's explain all of this:

`@DataJpaTest`:
- This annotation is used to test JPA repositories.
- It configures an in-memory database (like H2), scans for @Entity classes, and configures Spring Data JPA repositories.
- It also disables full auto-configuration and instead applies only configuration relevant to JPA tests.

- By default, each test method annotated with `@DataJpaTest` runs within a transactional boundary. 
- This ensures that changes made to the database are automatically rolled back at the end of the test, leaving a clean slate for the next test.

<details>
<summary>More about @DataJpaTest </summary>

- https://www.baeldung.com/junit-datajpatest-repository 

> The @DataJpaTest annotation is used to test JPA repositories in Spring Boot applications. It’s a specialized test annotation that provides a minimal Spring context for testing the persistence layer. This annotation can be used in conjunction with other testing annotations like @RunWith and @SpringBootTest.

> In addition, the scope of @DataJpaTest is limited to the JPA repository layer of the application. It doesn’t load the entire application context, which can make testing faster and more focused. This annotation also provides a pre-configured EntityManager and TestEntityManager for testing JPA entities.

</details>


`@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)`:
- This annotation specifies that the test should use an embedded H2 database.
- H2 is an in-memory database that is often used for testing because it is lightweight and fast.

`@RunWith(SpringRunner.class)`:
- This annotation tells JUnit to run the test with Spring's testing support.
- `SpringRunner` is an alias for `SpringJUnit4ClassRunner`, which integrates the Spring TestContext Framework with JUnit 4.
- In my case this is necessary to autowire Spring components (like PersonRepository) into the test class.
- `@RunWith(SpringRunner.class)` is necessary in JUnit 4 to initialize the Spring context and enable dependency injection.
- Without it, the Spring context is not loaded, and @Autowired dependencies remain null.

<details>
<summary>More about `@RunWith(SpringRunner.class)`</summary>

- https://stackoverflow.com/questions/58901288/springrunner-vs-springboottest 

`@RunWith(SpringRunner.class)` : You need this annotation to just enable spring boot features like @Autowire, @MockBean etc.. during junit testing

> is used to provide a bridge between Spring Boot test features and JUnit. Whenever we are using any Spring Boot testing features in our JUnit tests, this annotation will be required.

</details>

- So we injected the repository and satrt testing by the method `PersonRepository_saveAll_returnSavedPerson` which aims to test the save to database.


- `@Test`
This annotation marks the method as a test case that JUnit will run.

- **Arrange**: Prepare test data
- Here, we create a `Person` object using a builder pattern.
- The `Person` object is initialized with a first name, last name, and birth date.

- **Act**: Perform the action we want to test
- We call the `save` method on the `personRepository` to save the `Person` object to the database.
- The `save` method returns the saved `Person` object, which includes any auto-generated fields (like an ID).

- **Assert**: Verify the action result == test it
- We use assertions to verify that the `save` operation worked correctly:
- `assertThat(savedPerson).isNotNull();`: Checks that the saved `Person` object is not null.
- `assertThat(savedPerson.getId()).isPositive();`: Checks that the ID of the saved `Person` is a positive number (indicating that it was successfully generated by the database).
- `assertThat(savedPerson.getFirstName()).isEqualTo(person.getFirstName());`: Checks that the first name of the saved `Person` matches the first name we set initially.

- **Unit Tests naming conventions & best practices**:
> 1. The test name should describe what we want to test.For exmaple we used `PersonRepository_saveAll_returnSavedPerson` for our method, to indicate that we are testing the save method of the repo. respecting [Roy Osherove's naming strategy](https://osherove.com/blog/2005/4/3/naming-standards-for-unit-tests.html):
> **` [UnitOfWork_StateUnderTest_ExpectedBehavior]`** 
> For the classes, it's convenient to use the name of the tested class with `Test` or `Tests` as sufix (use the same for all test classes).

- So we just did out first unit test, lets continues.
- Let's new test `findAll` method of the repository: here is the code.

```java
@Test
    public void PersonRepository_findAll_returnMoreThanOnePerson(){
        // Arrange // prepare test data
        Person person1 = Person.builder()
                .firstName("person1fn")
                .lastName("person1ln")
                .birthDate(new Date(Date.valueOf("1999-01-07").getTime()))
                .build();
        Person person2 = Person.builder()
                .firstName("person2fn")
                .lastName("person2ln")
                .birthDate(new Date(Date.valueOf("2004-01-07").getTime()))
                .build();

        personRepository.saveAll(Lists.list(person1, person2));
        // Act // Perform test action

        List<Person> personList = personRepository.findAll();

        // Assert
        //Assertions.assertThat(personList).isNotNull();
        //Assertions.assertThat(personList).hasSize(2);
        // We can use them in one chain
        Assertions.assertThat(personList).isNotNull().hasSize(2);

    }
```
- All clear nothing needs to be explained.
- `Assertions` is a part of `Assertj`.
- Link: https://assertj.github.io/doc/#assertj-core 
- Here is an other example with a funny fact :smile:

```java
@Test
public void PersonRepository_findById_returnCorrectPerson(){
    // Arrange
        Person person2 = Person.builder()
                .firstName("person2fn")
                .lastName("person2ln")
                .birthDate(new Date(Date.valueOf("2004-01-07").getTime()))
                .build();

        personRepository.save(person2); // person2 will get its id

        // Act
        Person retrievedPerson = personRepository.findById(person2.getId()).get();

        // Assert
        Assertions.assertThat(person2).isNotNull()
                                        .isEqualTo(retrievedPerson);
}
```
- The Funny fact :fire::fire: 
> The `save()` method of the JpaRepository modifies the actual object passed to it (person2) by updating its id field with the value generated by the database.

- In our case after `personRepository.save(person2)` is called:
- The `id` field of `person2` is updated with the newly generated ID. that's why we used it directly in the Assertion without the need to affect the result of `save()` to a new Object.

![save_affect_actual_passed_obj.PNG](./imgs/save_affect_actual_passed_obj.PNG)

- Let's test a **Custom Query Method**
- Here our custom Query Method in the Repository `PersonRepository`.

```java
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> getPersonByFirstNameContaining(String fnKeyword);
}
```

- Here is the test in `PersonRepositoryTest`

```java
@Test
public void PersonRepository_getPersonByFirstNameContaining_returnListPersonWithFirstNameContainingKeyword(){
    // Arrange // prepare test data
    Person person1 = Person.builder()
            .firstName("person1fn")
            .lastName("person1ln")
            .birthDate(new Date(Date.valueOf("1999-01-07").getTime()))
            .build();
    Person person2 = Person.builder()
            .firstName("person2fn")
            .lastName("person2ln")
            .birthDate(new Date(Date.valueOf("2004-01-07").getTime()))
            .build();

    Person person3 = Person.builder()
            .firstName("differentFirstName")
            .lastName("person2ln")
            .birthDate(new Date(Date.valueOf("2004-01-07").getTime()))
            .build();

    personRepository.saveAll(Lists.list(person1, person2, person3));
    // Act // Perform test action

    List<Person> personList = personRepository.getPersonsByFirstNameContaining("per");

    // Assert
    Assertions.assertThat(personList).isNotNull().hasSize(2);
}
```
- The test is clear, simple and passed :ok:
- Visit code to see update and delete testing ... [link to PersonRepositoryTest.java](https://github.com/essadeq-elaamiri/getIntoMockitoForUnitTesting/blob/main/MockitoTutorial/src/test/java/me/elaamiri/MockitoTutorial/RepositoriesTests/PersonRepositoryTest.java)


##### Test the Service layer 
- Before that let's talk about `mocking`.

In the context of unit testing, **mocking** refers to the practice of creating simulated or "fake" objects that mimic the behavior of real objects in a controlled way. These simulated objects are called **mocks**. 

Mocking is used to isolate the unit of code being tested and ensure that the test focuses solely on the logic of that unit, without relying on or being affected by external dependencies (like databases, APIs, or other classes).

**Why is Mocking Used?**

1. **Isolation**
- Unit tests are meant to test a single unit of code (e.g., a method or class) in isolation.
- Mocking allows you to replace external dependencies (e.g., databases, web services, or other classes) with mock objects, so the test doesn't rely on those dependencies.

2. **Control**
- Mock objects allow you to define specific behaviors or responses for methods, making it easier to test different scenarios (e.g., success, failure, edge cases).

3. **Speed**
- Mocking avoids the overhead of interacting with real dependencies (e.g., making network calls or querying a database), making tests faster.

4. **Simplicity**
- Mocking simplifies tests by removing the need to set up complex environments or dependencies.


**Key Concepts in Mocking**

1. **Mock Object**
- A simulated object that mimics the behavior of a real object.
- For example, if you're testing a service that interacts with a database, you can mock the database repository to return predefined data instead of querying a real database.

2. **Stub**
- A type of mock object that provides predefined responses to method calls.
- For example, you can stub a method to always return a specific value or throw an exception.

3. **Spy**
- A partial mock that wraps a real object and allows you to verify interactions with it while still using its real behavior for some methods.

4. **Verification**
- Mocking frameworks often allow you to verify that specific methods were called with the expected arguments during the test.

- Confused! Let's practice this shit haha :smile:
- 


## Tutorial reference
- https://stackoverflow.com/questions/155436/unit-test-naming-best-practices 
- https://osherove.com/blog/2005/4/3/naming-standards-for-unit-tests.html
