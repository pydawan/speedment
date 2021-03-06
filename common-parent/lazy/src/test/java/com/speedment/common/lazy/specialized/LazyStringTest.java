/**
 *
 * Copyright (c) 2006-2019, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.speedment.common.lazy.specialized;

import com.speedment.common.lazy.AbstractLazyTest;
import com.speedment.common.lazy.Lazy;

/**
 * @author pemi
 */
public class LazyStringTest extends AbstractLazyTest<String> {

    @Override
    protected String firstValue() {
        return "A";
    }

    @Override
    protected String secondValue() {
        return "B";
    }

    @Override
    protected Lazy<String> newInstance() {
        return LazyString.create();
    }

    @Override
    protected String makeFromThread(Thread t) {
        return t.getName();
    }

}
