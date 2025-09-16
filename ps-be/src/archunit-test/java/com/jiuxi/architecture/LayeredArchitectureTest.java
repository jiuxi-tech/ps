package com.jiuxi.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit5.AnalyzeClasses;
import com.tngtech.archunit.junit5.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Assumptions;

@AnalyzeClasses(packages = "com.jiuxi", importOptions = ImportOption.DoNotIncludeTests.class)
public class LayeredArchitectureTest {

    private static boolean enforce() {
        // 通过 -DARCHUNIT_ENFORCE=true 显式开启校验；默认跳过
        return Boolean.parseBoolean(System.getProperty("ARCHUNIT_ENFORCE", "false"));
    }

    @ArchTest
    void domain_should_not_depend_on_infra_or_intf(JavaClasses classes) {
        Assumptions.assumeTrue(enforce(), "Skip ArchUnit enforcement by default");
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that().resideInAnyPackage("..module..domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..infra..", "..intf..");
        rule.check(classes);
    }

    @ArchTest
    void app_should_not_depend_on_infra_or_intf(JavaClasses classes) {
        Assumptions.assumeTrue(enforce(), "Skip ArchUnit enforcement by default");
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that().resideInAnyPackage("..app..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..infra..", "..intf..");
        rule.check(classes);
    }

    @ArchTest
    void intf_should_not_depend_on_infra(JavaClasses classes) {
        Assumptions.assumeTrue(enforce(), "Skip ArchUnit enforcement by default");
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that().resideInAnyPackage("..intf..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..infra..", "..module..infra..");
        rule.check(classes);
    }
}

