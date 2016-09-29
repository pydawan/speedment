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
package com.speedment.tool.internal.rule;

import com.speedment.common.injector.annotation.Inject;
import com.speedment.runtime.component.ProjectComponent;
import com.speedment.common.dbmodel.Document;
import com.speedment.common.dbmodel.Project;
import com.speedment.common.dbmodel.trait.HasAlias;
import com.speedment.common.dbmodel.util.DocumentDbUtil;
import com.speedment.tool.component.IssueComponent;
import com.speedment.tool.rule.Issue;
import com.speedment.tool.rule.Rule;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A rule which checks the future Java names of generated entities.
 * <p>
 * If a user would want an entity with the same name as a class used internally by the
 * the generated code, this might cause conflicts during runtime. Imagine for example
 * that the user would call his entity 'Integer'. Thus, we list all types
 * that normally show up during code generation. 
 * <P>
 * This rule is not perfect. The TypeMapper types are not included, and might cause
 * problems. For example, if someone names their entity Time, and that entity's SQL
 * type is Time, this might cause conflicts.
 *
 * @author Simon Jonasson
 * @since 3.0.0
 */
public class ProtectedNameRule implements Rule{
    private final static String[] PROTECTED_NAMES = {
        "AbstractEntity", "AbstractApplicationMetadata", "AbstractSqlManager",
        "Boolean",
        "Char", "Class", "ComparableField", "ComparableFieldImpl",
        "Double",
        "Field", "FieldIdentifier", "Float",
        "Integer", "Identifier", "Injector", 
        "Long",
        "Optional", "Object", "OptionalUtil",
        "ProjectComponent",
        "ResultSet",
        "String", "StringField", "StringFieldImpl", "SqlManager", "Stream", "StringBuilder", "SQLException", "SpeedmentException", "StringJoiner", "Speedment",
        "Table", "TypeMapper", "Tuple", "Tuples"           
    };
    
    private final Pattern pattern;
    
    private @Inject ProjectComponent projectComponent;
    private @Inject IssueComponent issues;
    
    public ProtectedNameRule(){
        String regex = Arrays.stream(PROTECTED_NAMES).collect(Collectors.joining("|"));
        this.pattern = Pattern.compile( regex , Pattern.CASE_INSENSITIVE );
    }

    @Override
    public CompletableFuture<Boolean> verify() {
        return CompletableFuture.supplyAsync( () -> checkRule() );
    }
    
    private boolean checkRule(){
        final AtomicBoolean noIssues = new AtomicBoolean(true);
        final Project project = projectComponent.getProject();  
        
        DocumentDbUtil.traverseOver(project)
            .forEach(doc -> check(doc, noIssues));
        
        return noIssues.get();
    }
    
    private void check(Document document, AtomicBoolean noIssues){
        final HasAlias alias = HasAlias.of(document);
        final String docName = alias.getJavaName();
        if( pattern.matcher( docName ).matches() ){
            noIssues.set(false);            
            issues.post( new Issue() {
                @Override
                public String getTitle() {
                    return "Protected Name: " + docName;
                }

                @Override
                public String getDescription() {
                    return "The Type name " + docName +" is used internally by Speedment."
                        + " If this name is assigned to a generated entiry, it might cause"
                        + " conflicts in the generated code.\n"
                        + "You may still proceed with code generation, but please be aware that"
                        + " the generated code might contain errors. To fix this issue, rename the"
                        + " entity in question.";
                }

                @Override
                public Issue.Level getLevel() {
                    return Level.WARNING;
                }
            } );
        }
    }
}