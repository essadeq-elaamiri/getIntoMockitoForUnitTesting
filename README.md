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
- We will cover unit testing in 3 layers:
    * Repository layer 
    * Service layer 
    * Controller layer 


#### Triple A (AAA) 
- AAA: Arrange, Act, Assert.=> How we arrange our Tests
- Called also (BDD): Behaviour Driven Development.


#### Start testing
- **AAA**: Arrange, Act, Assert: A common pattern for writing tests where you set up the test data, perform the action, and then verify the results.
1. **Arrange**: Prepare instances
2. **Act**: Do the action to be tested
3. **Assert**: Test the results 

![AAA](./imgs/AAA.PNG)

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

- **Arrange**
- Here, we create a `Person` object using a builder pattern.
- The `Person` object is initialized with a first name, last name, and birth date.

- **Act**
- We call the `save` method on the `personRepository` to save the `Person` object to the database.
- The `save` method returns the saved `Person` object, which includes any auto-generated fields (like an ID).

- **Assert**
We use assertions to verify that the `save` operation worked correctly:

- `assertThat(savedPerson).isNotNull();`: Checks that the saved `Person` object is not null.
- `assertThat(savedPerson.getId()).isPositive();`: Checks that the ID of the saved `Person` is a positive number (indicating that it was successfully generated by the database).
- `assertThat(savedPerson.getFirstName()).isEqualTo(person.getFirstName());`: Checks that the first name of the saved `Person` matches the first name we set initially.

- **Unit Tests naming conventions & best practices**:
> 1. The test name should describe what we want to test.For exmaple we used `PersonRepository_saveAll_returnSavedPerson` for our method, to indicate that we are testing the save method of the repo. respecting [Roy Osherove's naming strategy](https://osherove.com/blog/2005/4/3/naming-standards-for-unit-tests.html):
> **` [UnitOfWork_StateUnderTest_ExpectedBehavior]`** 
> For the classes, it's convenient to use the name of the tested class with `Test` or `Tests` as sufix (use the same for all test classes).

## Tutorial reference
