/*
 * Copyright (C) 2014 Avram Lyon (ajlyon@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gimranov.libzotero.rx;

import com.gimranov.libzotero.persist.IPersistence;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
//import retrofit.RetrofitError;
//import rx.Observable;
//import rx.functions.Func1;

public class PersistenceLoadFunc<T> implements Function<Throwable, Observable<T>> {

    private Class<T> tClass;
    private IPersistence persistence;

    public PersistenceLoadFunc(Class<T> tClass, IPersistence persistence) {
        this.tClass = tClass;
        this.persistence = persistence;
    }

    @Override
    public Observable<T> apply(Throwable throwable) {
//        if (throwable instanceof RetrofitError) {
//            if (((RetrofitError) throwable).getResponse().getStatus() == 304) {
//                return persistence.load(tClass);
//            }
//        }
        return Observable.error(throwable);
    }
}
