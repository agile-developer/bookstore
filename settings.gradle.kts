rootProject.name = "bookstore"
include("${rootProject.name}-app")
project(":${rootProject.name}-app").projectDir = mkdir("${rootProject.projectDir.path}/app")
include("${rootProject.name}-third-party-stubs")
project(":${rootProject.name}-third-party-stubs").projectDir = mkdir("${rootProject.projectDir.path}/third-party-stubs")
