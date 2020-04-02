package net.wurstclient.fmlevents.sigmaevent;

public interface EventListener<E extends Event>
{
    void onEvent(E event);
}
