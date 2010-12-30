import sbt._
import de.element34.sbteclipsify._

trait Defaults extends Eclipsify {
  def androidPlatformName = "android-8"
}
  
class HttpService(info: ProjectInfo) extends ParentProject(info) {
  
  val codehaus = "codehaus Repository" at "http://repository.codehaus.org/org/codehaus/jackson"
  val sonatype = "Sonatype Release" at "http://oss.sonatype.org/content/repositories/releases"
  val jbossrepo = "repository.jboss.org" at "http://repository.jboss.org/nexus/content/groups/public/"
  
  override def shouldCheckOutputDirectories = false
  override def updateAction = task { None }
  
  lazy val main  = project(".", "HttpService", new MainProject(_))
  lazy val tests = project("tests" / "instrumentation",  "HttpServiceTest", new TestProject(_), main)
  
  class MainProject(info: ProjectInfo) extends AndroidProject(info) with Defaults with Robolectric {        
    val jacksoncore = "org.codehaus.jackson" % "jackson-core-asl" % "1.6.2" % "compile"
  	val jacksonmapper = "org.codehaus.jackson" % "jackson-mapper-asl" % "1.6.2" % "compile"
  	val asyncclient = "com.ning" % "async-http-client" % "1.4.1" % "compile"


       val awaitility = "com.jayway.awaitility" % "awaitility-scala" % "1.3.1" % "test"
       val cglib = "cglib" % "cglib-nodep" % "2.2" % "test"
       val hamcrestcore = "org.hamcrest" % "hamcrest-core" % "1.1" % "test"
       val hamcrestlibrary = "org.hamcrest" % "hamcrest-library" % "1.1" % "test"
      val objenisis = "org.objenesis" % "objenesis" % "1.2" % "test"
val roboelectric = "org.robolectric" % "robolectric" % "0.9.4" % "test" from "http://pivotal.github.com/robolectric/downloads/robolectric-0.9.4.jar"


    val signpostcore = "oauth.signpost" % "signpost-core" % "1.2.1" % "compile"
    val signpostcommons = "oauth.signpost" % "signpost-commonshttp4" % "1.2.1" % "compile"
    def googleMapLocation =  androidSdkPath  / "add-ons" / "addon_google_apis_google_inc_8" / "libs" / "maps.jar" absolutePath
  }
      
  class TestProject(info: ProjectInfo) extends AndroidTestProject(info) with Defaults {
    val robotium = "com.jayway.android.robotium" % "robotium-solo" % "1.9.0" % "test"
  }  
}
