package pl.polidea.robospock

import org.robolectric.Robolectric
import org.robolectric.annotation.Config
import pl.polidea.robospock.activity.MainActivity
import pl.polidea.robospock.shadow.MyActivityManagerShadow

@Config(shadows = [MyActivityManagerShadow])
class ShadowSpec extends RoboSpecification {

    def "shouldCompile"() {
        given:
        def mainActivity = Robolectric.buildActivity(MainActivity).create().get()

        when:
        def text = mainActivity.memoryTv.text

        then:
        text == "I have 16 MB"
    }
}