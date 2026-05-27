package wemade.ontongsal.springmodulith

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.docs.Documenter


@SpringBootTest
class SpringModulithApplicationTests {

	@Test
	fun contextLoads() {
	}

    @Test
    fun writeDocumentationSnippets() {
        var modules: ApplicationModules = ApplicationModules.of(SpringModulithApplication::class.java).verify()

        Documenter(modules)
            .writeModulesAsPlantUml()
            .writeIndividualModulesAsPlantUml()
    }

}
