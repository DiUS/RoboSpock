package pl.polidea.robospock;


import com.google.inject.AbstractModule
import com.google.inject.Module
import com.google.inject.util.Modules
import org.junit.runner.RunWith
import roboguice.RoboGuice
import spock.lang.Specification

@RunWith(RoboSputnik)
@UseShadows
public abstract class RoboSpecification extends Specification {

    Set<Class<? extends Module>> moduleClasses = [];

    void inject(Closure closure) {


        inject(new AbstractModule() {

            void bind(Class superClass, Class clazz) {
                if (superClass.isAssignableFrom(clazz)) {
                    bind(superClass).to(clazz)
                } else {
                    addError("Instance class " + clazz.getName() + " can't be mapped to " + superClass.getName())
                }
            }

            void bindInstance(Class superClass, Object object) {
                bind(superClass).toInstance(object)
            }

            void install(Class c) {
                def module = c.newInstance()
                if (module instanceof AbstractModule) {
                    install(module)
                } else {
                    addError("Installed class: " + c.getName() + " it's not Guice Abstract Module")
                }
            }

            @Override
            protected void configure() {
                closure.delegate = this
                closure.call()
            }
        })
    }

    void inject(Module... modules) {

        moduleClasses.addAll(modules)

        RoboGuice.setBaseApplicationInjector(
                Robolectric.application,
                RoboGuice.DEFAULT_STAGE,
                Modules.override(
                        RoboGuice.newDefaultRoboModule(Robolectric.application)
                ).with(moduleClasses)
        );

        RoboGuice.injectMembers(Robolectric.application, this)
    }

}
