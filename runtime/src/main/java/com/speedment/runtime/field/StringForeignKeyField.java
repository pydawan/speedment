/**
 *
 * Copyright (c) 2006-2016, Speedment, Inc. All Rights Reserved.
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
package com.speedment.runtime.field;


import com.speedment.runtime.field.trait.HasFinder;

/**
 * A field that represents a string column with a foreign key to 
 * another column.
 * 
 * @param <ENTITY>     the entity type
 * @param <D>          the database type
 * @param <FK_ENTITY>  the foreign entity type
 * 
 * @author  Per Minborg
 * @author  Emil Forslund
 * @since   2.2.0
 * 
 * @see  StringField
 * @see  HasFinder
 */

public interface StringForeignKeyField<ENTITY, D, FK_ENTITY> extends
    StringField<ENTITY, D>, 
    HasFinder<ENTITY, FK_ENTITY> {}