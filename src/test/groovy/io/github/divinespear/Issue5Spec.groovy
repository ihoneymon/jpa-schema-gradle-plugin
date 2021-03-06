package io.github.divinespear

import org.gradle.test.FunctionalSpec

class Issue5Spec extends FunctionalSpec {

    def setup() {
        buildFile << applyPlugin(JpaSchemaGeneratePlugin)
    }
    
    def shouldWorkHibernateWithValidationAPI() {
        given:
        buildFile << """
            sourceSets {
                main {
                    java {
                        srcDir file("../../../../src/test/resources/unit/hibernate/src")
                    }
                    resources {
                        srcDir file("../../../../src/test/resources/unit/hibernate/resources")
                    }
                    output.resourcesDir output.classesDir
                }
            }


            repositories {
                mavenCentral()
            }
            
            
            dependencies {
                compile 'org.hibernate:hibernate-entitymanager:4.3.4.Final'
                compile 'javax.validation:validation-api:1.1.0.Final'
                runtime 'org.hibernate:hibernate-validator:5.1.0.Final'
            }
            
            generateSchema {
                namingStrategy = "org.hibernate.cfg.ImprovedNamingStrategy"
                format = true
                scriptAction = "drop-and-create"
                databaseProductName = "H2"
                databaseMajorVersion = 1
                databaseMinorVersion = 3
            }
        """
        when:
        run "generateSchema"
        then:
        file("build/generated-schema/create.sql").exists()
        file("build/generated-schema/create.sql").text == """create table key_value_store (
\tstored_key varchar(128) not null,
\tcreated_at timestamp,
\tstored_value varchar(32768),
\tprimary key (stored_key)
);

create table many_column_table (
\tid bigint generated by default as identity,
\tcolumn00 varchar(255),
\tcolumn01 varchar(255),
\tcolumn02 varchar(255),
\tcolumn03 varchar(255),
\tcolumn04 varchar(255),
\tcolumn05 varchar(255),
\tcolumn06 varchar(255),
\tcolumn07 varchar(255),
\tcolumn08 varchar(255),
\tcolumn09 varchar(255),
\tcolumn10 varchar(255),
\tcolumn11 varchar(255),
\tcolumn12 varchar(255),
\tcolumn13 varchar(255),
\tcolumn14 varchar(255),
\tcolumn15 varchar(255),
\tcolumn16 varchar(255),
\tcolumn17 varchar(255),
\tcolumn18 varchar(255),
\tcolumn19 varchar(255),
\tcolumn20 varchar(255),
\tcolumn21 varchar(255),
\tcolumn22 varchar(255),
\tcolumn23 varchar(255),
\tcolumn24 varchar(255),
\tcolumn25 varchar(255),
\tcolumn26 varchar(255),
\tcolumn27 varchar(255),
\tcolumn28 varchar(255),
\tcolumn29 varchar(255),
\tprimary key (id)
);

"""
        file("build/generated-schema/drop.sql").exists()
        file("build/generated-schema/drop.sql").text == """drop table key_value_store if exists;

drop table many_column_table if exists;

"""
    }
}
