package net.wurstclient.fmlevents.sigmaevent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.client.Minecraft;

public class EventSubscription<T extends Event>
{
    private final T event;
    private final List<EventListener> subscribed = new CopyOnWriteArrayList<>();

    public EventSubscription(T event)
    {
        this.event = event;
    }

    public void fire(Event event)
    {
        if (Minecraft.getMinecraft().player == null)
        {
            return;
        }

        subscribed.forEach(listener -> listener.onEvent(event));
    }

    public void add(EventListener listener)
    {
        subscribed.add(listener);
    }

    public void remove(EventListener listener)
    {
        if (subscribed.contains(listener))
        {
            subscribed.remove(listener);
        }
    }

    public List<EventListener> getSubscribed()
    {
        return subscribed;
    }

    public Event getEvent()
    {
        return event;
    }
}

