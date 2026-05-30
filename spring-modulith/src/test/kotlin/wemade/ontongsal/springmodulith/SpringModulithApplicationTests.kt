package wemade.ontongsal.springmodulith

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter
import org.springframework.test.context.ActiveProfiles


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringModulithApplicationTests {

	@Test
	fun contextLoads() {
	}

    @Test
    fun writeDocumentationSnippets() {
        val modules: ApplicationModules = ApplicationModules.of(SpringModulithApplication::class.java).verify()

        Documenter(modules)
            .writeModulesAsPlantUml()
            .writeIndividualModulesAsPlantUml()
    }

}
