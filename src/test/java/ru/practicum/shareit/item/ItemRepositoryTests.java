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
}
