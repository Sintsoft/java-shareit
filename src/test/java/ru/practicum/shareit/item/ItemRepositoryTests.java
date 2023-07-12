package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.TestDataGenerator;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.vault.ItemRepository;
import ru.practicum.shareit.utility.configuration.PersistenceConfig;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Import({PersistenceConfig.class})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
        "classpath:reset_db.sql",
        "classpath:item_test_prep.sql"})
public class ItemRepositoryTests {

    @Autowired
    ItemRepository testRepo;

    @Autowired
    private TestEntityManager testEM;

    @Test
    void saveItemTest() {
        Item testItem = testRepo.save(ItemMapper.fromDto(
                TestDataGenerator.generateTestRequestItemDTO(1L, null),
                TestDataGenerator.generateTestUser(1L),
                TestDataGenerator.generateTestItemRequest(1L, 2L)));

        assertEquals(1L, testItem.getId());
    }

    @Test
    void updateItemTest() {
        Item testItem = testRepo.save(ItemMapper.fromDto(
                TestDataGenerator.generateTestRequestItemDTO(1L, null),
                TestDataGenerator.generateTestUser(1L),
                TestDataGenerator.generateTestItemRequest(1L, 2L)));
        testItem.setName("new item name");
        testItem.setAvailable(false);
        testItem = testRepo.save(testItem);

        assertEquals(1L, testItem.getId());
        assertEquals("new item name", testItem.getName());
        assertEquals("item1 description", testItem.getDescription());
        assertFalse(testItem.getAvailable());
    }

    @Test
    void findItemTest() {
        testRepo.save(ItemMapper.fromDto(
                TestDataGenerator.generateTestRequestItemDTO(1L, null),
                TestDataGenerator.generateTestUser(1L),
                TestDataGenerator.generateTestItemRequest(1L, 2L)));

        Item testItem = testRepo.findById(1L).get();

        assertEquals(1L, testItem.getId());
    }

    @Test
    void findAllItemsTest() {
        testRepo.save(ItemMapper.fromDto(
                TestDataGenerator.generateTestRequestItemDTO(1L, null),
                TestDataGenerator.generateTestUser(1L),
                TestDataGenerator.generateTestItemRequest(1L, 2L)));

        assertEquals(1, testRepo.findAll().size());

        testRepo.save(ItemMapper.fromDto(
                TestDataGenerator.generateTestRequestItemDTO(2L, null),
                TestDataGenerator.generateTestUser(2L),
                null));

        assertEquals(2, testRepo.findAll().size());
    }

    @Test
    void searchItemTest() {
        testRepo.save(ItemMapper.fromDto(
                TestDataGenerator.generateTestRequestItemDTO(1L, 1L),
                TestDataGenerator.generateTestUser(1L),
                TestDataGenerator.generateTestItemRequest(1L, 2L)));
        testRepo.save(ItemMapper.fromDto(
                TestDataGenerator.generateTestRequestItemDTO(2L, 1L),
                TestDataGenerator.generateTestUser(1L),
                TestDataGenerator.generateTestItemRequest(1L, 2L)));

        assertEquals(1, testRepo.seachForItems("%Em1%", 0, 10).size());
    }

    @Test
    void findUserItems() {
        testRepo.save(ItemMapper.fromDto(
                TestDataGenerator.generateTestRequestItemDTO(1L, null),
                TestDataGenerator.generateTestUser(1L),
                null));
        testRepo.save(ItemMapper.fromDto(
                TestDataGenerator.generateTestRequestItemDTO(2L, null),
                TestDataGenerator.generateTestUser(2L),
                null));

        assertEquals(1, testRepo.findUserItems(1L, 0, 10).size());
        assertEquals(1, testRepo.findUserItems(2L, 0, 10).size());
        assertEquals(2, testRepo.findAll().size());
    }

    @Test
    void findRequestItems() {
        testRepo.save(ItemMapper.fromDto(
                TestDataGenerator.generateTestRequestItemDTO(1L, 1L),
                TestDataGenerator.generateTestUser(1L),
                TestDataGenerator.generateTestItemRequest(1L, 2L)));
        testRepo.save(ItemMapper.fromDto(
                TestDataGenerator.generateTestRequestItemDTO(2L, 2L),
                TestDataGenerator.generateTestUser(2L),
                TestDataGenerator.generateTestItemRequest(2L, 1L)));

        assertEquals(1, testRepo.findRequestItems(1L, 0, 10).size());
        assertEquals(1, testRepo.findRequestItems(2L, 0, 10).size());
        assertEquals(2, testRepo.findAll().size());
    }


}
