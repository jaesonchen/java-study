package com.asiainfo.designpattern.behavior;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义对象间的一种一对多的依赖关系,当一个对象的状态发生改变时, 所有依赖于它的对象都得到通知并被自动更新。
 * 
 * @author       zq
 * @date         2017年12月21日  下午5:36:33
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class Observer {

    /** 
     * TODO
     * 
     * @param args
     */
    public static void main(String[] args) {

        Subject subject = new MySubject();
        subject.attach(new Listener() {
            @Override
            public void update(Event event) {
                System.out.println("Listener 1 receive update event, status=" + event.status);
            }
        });
        subject.attach(new Listener() {
            @Override
            public void update(Event event) {
                System.out.println("Listener 2 receive update event, status=" + event.status);
            }
        });
        Listener listener = new Listener() {
            @Override
            public void update(Event event) {
                System.out.println("Listener 3 receive update event, status=" + event.status);
            }
        };
        subject.attach(listener);
        subject.setState(1);
        System.out.println("=====================");
        subject.detach(listener);
        subject.setState(2);
    }
    static class Event {
        int status;
        Event(int status) {
            this.status = status;
        }
    }
    interface Listener {
        void update(Event event);
    }
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
    }
    
    static class MySubject extends Subject {
        
        int status = 0;
        @Override
        void setState(int status) {
            this.status = status;
            Event event = new Event(status);
            for (Listener listener : super.listeners) {
                listener.update(event);
            }
        }
    }
}
