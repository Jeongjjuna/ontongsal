package yjh.ontongsal.restapi

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

@AnalyzeClasses(
    packages = ["yjh.ontongsal.restapi"],
    importOptions = [ImportOption.DoNotIncludeTests::class],
)
class ArchitectureTest {

    @ArchTest
    val domainHasNoSpringDependency: ArchRule = noClasses()
        .that().resideInAPackage("..domain..")
        .should().dependOnClassesThat().resideInAPackage("org.springframework..")
        .`as`("도메인 계층은 Spring에 의존하지 않아야 한다")

    @ArchTest
    val domainHasNoApplicationDependency: ArchRule = noClasses()
        .that().resideInAPackage("..domain..")
        .should().dependOnClassesThat().resideInAPackage("..application..")
        .`as`("도메인 계층은 애플리케이션 계층에 의존하지 않아야 한다")

    @ArchTest
    val applicationHasNoAdapterDependency: ArchRule = noClasses()
        .that().resideInAPackage("..application..")
        .should().dependOnClassesThat().resideInAPackage("..adapter..")
        .`as`("애플리케이션 계층은 어댑터 계층에 의존하지 않아야 한다")

    @ArchTest
    val controllersShouldBeInWebPackage: ArchRule = classes()
        .that().haveSimpleNameEndingWith("Controller")
        .should().resideInAPackage("..web..")
        .`as`("Controller 클래스는 web 패키지에 위치해야 한다")
}
