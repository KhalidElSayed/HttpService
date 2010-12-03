import sbt._
import de.element34.sbteclipsify._

trait Defaults extends Eclipsify {
  def androidPlatformName = "android-8"
}
  
class HttpService(info: ProjectInfo) extends ParentProject(info) {
  
  val codehaus = "codehaus Repository" at "http://repository.codehaus.org/org/codehaus/jackson"
  
  override def shouldCheckOutputDirectories = false
  override def updateAction = task { None }
  
  lazy val main  = project(".", "HttpService", new MainProject(_))
  lazy val tests = project("tests" / "instrumentation",  "HttpServiceTest", new TestProject(_), main)
  
  class MainProject(info: ProjectInfo) extends AndroidProject(info) with Defaults with Robolectric {        
   val jacksoncore = "org.codehaus.jackson" % "jackson-core-asl" % "1.6.2" % "compile"
  val jacksonmapper = "org.codehaus.jackson" % "jackson-mapper-asl" % "1.6.2" % "compile"
    def googleMapLocation =  androidSdkPath  / "add-ons" / "addon_google_apis_google_inc_8" / "libs" / "maps.jar" absolutePath
  }
      
  class TestProject(info: ProjectInfo) extends AndroidTestProject(info) with Defaults {
  }  
}
