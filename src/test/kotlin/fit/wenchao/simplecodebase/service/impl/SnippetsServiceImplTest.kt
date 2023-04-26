package fit.wenchao.simplecodebase.service.impl

import fit.wenchao.simplecodebase.dao.po.CreateSnippet
import fit.wenchao.simplecodebase.service.SnippetsService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SnippetsServiceImplTest {

    @Autowired
    lateinit var snippetsService: SnippetsService

    @Test
    fun save() {
        var snippet = CreateSnippet()

        snippetsService.create(snippet)
    }
}