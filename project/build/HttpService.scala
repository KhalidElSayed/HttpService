import sbt._

trait Defaults {
  def androidPlatformName = "android-8"
}

class HttpService(info: ProjectInfo) extends ParentProject(info) {
  val codehaus = "codehaus Repository" at "http://repository.codehaus.org/org/codehaus/jackson"
  val sonatype = "Sonatype Release" at "http://oss.sonatype.org/content/repositories/releases"

  override def shouldCheckOutputDirectories = false
  override def updateAction = task { None }

  lazy val main = project(".", "HttpService", new MainProject(_))
  lazy val tests = project("tests" / "instrumentation", "HttpServiceTest", new TestProject(_), main)

  class MainProject(info: ProjectInfo) extends AndroidProject(info) with Defaults with Robolectric with Eclipse {
    val jacksoncore = "org.codehaus.jackson" % "jackson-core-asl" % "1.6.2" % "compile"
    val jacksonmapper = "org.codehaus.jackson" % "jackson-mapper-asl" % "1.6.2" % "compile"
    val signpostcore = "oauth.signpost" % "signpost-core" % "1.2.1" % "compile"
    val signpostcommons = "oauth.signpost" % "signpost-commonshttp4" % "1.2.1" % "compile"
    def googleMapLocation = androidSdkPath / "add-ons" / "addon_google_apis_google_inc_8" / "libs" / "maps.jar" absolutePath
  }

  class TestProject(info: ProjectInfo) extends AndroidTestProject(info) with Defaults with Eclipse {
    val robotium = "com.jayway.android.robotium" % "robotium-solo" % "1.9.0" % "test"
  }
}
