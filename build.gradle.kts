plugins {
    id("dev.frozenmilk.teamcode") version "11.0.0-1.1.0"
}

repositories {
    maven("https://central.sonatype.com/repository/maven-snapshots")
}

ftc {
    // adds the necessary sdk dependencies
    sdk.TeamCode()

    dairy {
        implementation(Sloth)
        implementation(slothboard)
        implementation(ftControl.fullpanels)
    }

    pedro {
        implementation(ftc("2.1.0-SNAPSHOT"))
        implementation(telemetry)
    }
}

dependencies {
    implementation("com.github.haifengl:smile-interpolation:2.6.0") {
        exclude(group = "org.slf4j")
    }
    implementation("org.solverslib:core:0.3.3")
    implementation("org.solverslib:pedroPathing:0.3.3")
}

configurations.configureEach {
    resolutionStrategy.cacheChangingModulesFor(67, "days")
}