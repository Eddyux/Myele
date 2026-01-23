pluginManagement {
    repositories {
        // 阿里云镜像源（优先使用）
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/jcenter") }

        // 腾讯云镜像源（备用）
        maven { url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/") }

        // 华为云镜像源（备用）
        maven { url = uri("https://repo.huaweicloud.com/repository/maven/") }

        // 官方仓库（最后使用）
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // 阿里云镜像源（优先使用）
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://maven.aliyun.com/repository/jcenter") }

        // 腾讯云镜像源（备用）
        maven { url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/") }

        // 华为云镜像源（备用）
        maven { url = uri("https://repo.huaweicloud.com/repository/maven/") }

        // 官方仓库（最后使用）
        mavenCentral()
        google()
    }
}

rootProject.name = "eleme"
include(":app")