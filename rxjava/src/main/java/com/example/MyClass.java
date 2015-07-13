package com.example;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MyClass {


    public static void main(String[] args) {
        MyClass myClass = new MyClass();
        myClass.part2_2();

    }

    public void sample1() {
        Observable<String> myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        sub.onNext("Super long Observable! Boo Yah!");
                        sub.onCompleted();
                    }
                }
        );

        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                System.out.println(s);
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }
        };

        myObservable.subscribe(mySubscriber);

    }

    public void sample2() {
        Observable<String> myObservable =
                Observable.just("Observable using just");

        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        };

        myObservable.subscribe(onNextAction);
    }

    public void sample3() {
        Observable.just("All together Woo hoo")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(s);
                    }
                });
    }

    // ===========================
    // Part 2
    // ===========================
    private Observable<List<String>> query(String text) {
        List<String> listOfStrings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            listOfStrings.add(String.format("Url[%s]", i));
        }
        return Observable.just(listOfStrings);
    }

    public void part2_1() {
        // Returns a List of website URLs based on a text search

        query("Hello, world!")
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> urls) {
                        for (String url : urls) {
                            System.out.println(url);
                        }
                    }
                });
    }

    public void part2_2() {
        // Returns a List of website URLs based on a text search

        query("Hello, world!")
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> urls) {
                        Observable.from(urls).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                System.out.println(s);
                            }
                        });
                    }
                });
    }

    public void part2_3() {
        query("Hello, world!")
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> urls) {
                        return Observable.from(urls);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(s);
                    }
                });
    }

    public void part2_4() {
        query("Hello, world!")
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> urls) {
                        return Observable.from(urls);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String url) {
                        return getTitle(url);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(s);
                    }
                });
    }

    private Observable<String> getTitle(String url) {
        return Observable.just("Title - " + url);
    }

    public void part2_5() {
        query("Hello, world!")
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> urls) {
                        return Observable.from(urls);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String url) {
                        return getTitle(url);
                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s != null && (s.contains("2") || s.contains("4"));
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(s);
                    }
                });
    }

    public void part2_6() {
        query("Hello, world!")
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> urls) {
                        return Observable.from(urls);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String url) {
                        return getTitle(url);
                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s != null && (s.contains("2") || s.contains("4"));
                    }
                })
                .take(5)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(s);
                    }
                });
    }

    public void part3_1() {
        query("blah")
                .map(new Func1<List<String>, String>() {
                    @Override
                    public String call(List<String> strings) {
                        return "possibleerror";
                    }
                })
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return 9999;
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("Completed!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("Ouch!");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(integer);
                    }
                });
    }

    public void part3_2() {
        query("Hello, world!")
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> urls) {
                        return Observable.from(urls);
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.immediate())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(s);
                    }
                });
    }

}
