package com.asiainfo.designpattern.behavior;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义对象间的一对多的依赖关系,当一个对象的状态发生改变时, 所有依赖于它的对象都得到通知并被自动更新。
 * 典型应用：监听器Listener
 * 
 * @author       zq
 * @date         2017年12月21日  下午5:36:33
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Observer {

    public static void main(String[] args) {

        Subject subject = new MySubject();
        subject.attach(new Listener() {
            @Override
            public void onUpdate(Event event) {
                if (EventType.STARTUP == event.getEventType()) {
                    System.out.println("Listener 1 receive startup event, event=" + event.getEventType());
                }
            }
        });
        Listener listener = new Listener() {
            @Override
            public void onUpdate(Event event) {
                if (EventType.STATUS_CHANGE == event.getEventType()) {
                    System.out.println("Listener 2 receive update event, status=" + event.status);
                }
            }
        };
        subject.attach(listener);
        subject.attach(new Listener() {
            @Override
            public void onUpdate(Event event) {
                if (EventType.SHUTDOWN == event.getEventType()) {
                    System.out.println("Listener 3 receive shutdown event, event=" + event.getEventType());
                }
            }
        });
        
        subject.startup();
        subject.setState(1);
        System.out.println("=====================");
        subject.detach(listener);
        subject.setState(2);
        subject.shutdown();
    }
    
    enum EventType {
        STATUS_CHANGE, STARTUP, SHUTDOWN;
    }
    // 事件定义
    static class Event {
        EventType type;
        int status;
        Event(EventType type) {
            this.type = type;
        }
        Event(EventType type, int status) {
            this.type = type;
            this.status = status;
        }
        EventType getEventType() {
            return type;
        }
    }
    // 监听器接口
    interface Listener {
        void onUpdate(Event event);
    }
    // 被观察的主题抽象实现
    abstract static class Subject {
        
        protected List<Listener> listeners = new ArrayList<>();
        public void attach(Listener listener) {
            if (!this.listeners.contains(listener)) {
                this.listeners.add(listener);
            }
        }
        public void detach(Listener listener) {
            if (this.listeners.contains(listener)) {
                this.listeners.remove(listener);
            }
        }
        abstract void setState(int status);
        abstract void startup();
        abstract void shutdown();
    }
    // 被观察的主题具体实现
    static class MySubject extends Subject {
        
        int status = 0;
        @Override
        void setState(int status) {
            System.out.println("MySubject status change, status = " + status);
            this.status = status;
            fireEvent(new Event(EventType.STATUS_CHANGE, status));
        }
        @Override
        void startup() {
            System.out.println("MySubject startup!");
            fireEvent(new Event(EventType.STARTUP));
        }
        @Override
        void shutdown() {
            System.out.println("MySubject shutdown!");
            fireEvent(new Event(EventType.SHUTDOWN));
        }
        protected void fireEvent(Event event) {
            for (Listener listener : super.listeners) {
                listener.onUpdate(event);
            }
        }
    }
}
