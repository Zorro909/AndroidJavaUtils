package JavaUtils.Events;

import java.awt.Window.Type;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class EventManager {

	private static HashMap<Class<?extends Event>,HashMap<EventPriority,ArrayList<Method>>> listeners = new HashMap<Class<?extends Event>,HashMap<EventPriority,ArrayList<Method>>>();
	private static HashMap<Method, Object> objects = new HashMap<Method,Object>();
	
	public static void registerListeners(Object o){
		for(Method m : o.getClass().getMethods()){
			if(m.isAnnotationPresent(EventListener.class)){
				if(m.getParameterCount()==1 && Event.class.isAssignableFrom(m.getParameterTypes()[0])){
					EventListener el = m.getAnnotation(EventListener.class);
					EventPriority ep = el.value();
					HashMap<EventPriority, ArrayList<Method>> hm = new HashMap<EventPriority,ArrayList<Method>>();
					if(listeners.containsKey(m.getParameterTypes()[0])){
						hm = listeners.get(m.getParameterTypes()[0]);
					}
					ArrayList<Method> h  = new ArrayList<Method>();
					if(hm.containsKey(ep)){
						h = hm.get(ep);
					}
					h.add(m);
					hm.put(ep, h);
					objects.put(m, o);
					listeners.put((Class<? extends Event>) m.getParameterTypes()[0], hm);
				}
			}
		}
	}
	
	public static void fireEvent(Event e){
		if(!listeners.containsKey(e.getClass()))return;
		try{
		HashMap<EventPriority,ArrayList<Method>> hm = listeners.get(e.getClass());
		for(int i = 0;i<EventPriority.values().length;i++){
			EventPriority ep = EventPriority.values()[i];
			if(!hm.containsKey(ep))continue;
			for(Method m : hm.get(ep)){
				m.invoke(objects.get(m), e);
			}
		}
		}catch(Exception ex){
			
		}
	}
	
}
