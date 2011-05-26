/*
 * Copyright 2011 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drools.ide.common.client.modeldriven.dt;

import java.util.HashMap;
import java.util.Map;

import org.drools.ide.common.client.modeldriven.brl.CEPWindow;
import org.drools.ide.common.client.modeldriven.brl.HasCEPWindow;
import org.drools.ide.common.client.modeldriven.brl.HasParameterizedOperator;

/**
 * This is the config for a condition column. Typically many of them have their
 * constraints added.
 */
public class ConditionCol extends DTColumnConfig
    implements
    HasParameterizedOperator,
    HasCEPWindow {

    private static final long   serialVersionUID = 510l;

    // What is displayed at the top
    private String              header;

    // The type of the fact - class - eg Driver, Person, Cheese etc.
    private String              factType;

    // The name that this gets referenced as. Multiple columns with the same
    // name mean their constraints will be combined.
    private String              boundName;

    // The type of the value that is in the cell, eg if it is a formula, or
    // literal value etc. The valid types are from ISingleFieldConstraint:
    // TYPE_LITERAL TYPE_RET_VALUE TYPE_PREDICATE (in this case, the field and
    // operator are ignored).
    private int                 constraintValueType;

    // The field of the fact that this pertains to (if its a predicate, ignore it).
    private String              factField;

    // The data-type of the field in the Fact used in the Condition. Possible
    // values are held within the SuggestionCompletionEngine.TYPE_XXX
    private String              fieldType;

    // The operator to use to compare the field with the value (unless its a
    // predicate, in which case this is ignored).
    private String              operator;

    // Whether the pattern should be negated
    private boolean             isNegated;

    // A comma separated list of valid values. Optional.
    private String              valueList;

    //CEP operators' parameters
    private Map<String, String> parameters;

    //CEP 'window' definition
    private CEPWindow           window;

    //Entry-point name
    private String              entryPointName;

    public void setHeader(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setFactType(String factType) {
        this.factType = factType;
    }

    public String getFactType() {
        return factType;
    }

    public void setBoundName(String boundName) {
        this.boundName = boundName;
    }

    public String getBoundName() {
        return boundName;
    }

    public void setConstraintValueType(int constraintValueType) {
        this.constraintValueType = constraintValueType;
    }

    public int getConstraintValueType() {
        return constraintValueType;
    }

    public void setFactField(String factField) {
        this.factField = factField;
    }

    public String getFactField() {
        return factField;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public void setValueList(String valueList) {
        this.valueList = valueList;
    }

    public String getValueList() {
        return valueList;
    }

    public boolean isNegated() {
        return isNegated;
    }

    public void setNegated(boolean negated) {
        this.isNegated = negated;
    }

    public void clearParameters() {
        this.parameters = null;
    }

    public String getParameter(String key) {
        if ( parameters == null ) {
            return null;
        }
        String parameter = parameters.get( key );
        return parameter;
    }

    public void setParameter(String key,
                             String parameter) {
        if ( parameters == null ) {
            parameters = new HashMap<String, String>();
        }
        parameters.put( key,
                        parameter );
    }

    public void deleteParameter(String key) {
        if ( this.parameters == null ) {
            return;
        }
        parameters.remove( key );
    }

    public Map<String, String> getParameters() {
        if ( this.parameters == null ) {
            this.parameters = new HashMap<String, String>();
        }
        return this.parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public void setWindow(CEPWindow window) {
        this.window = window;
    }

    public CEPWindow getWindow() {
        if ( this.window == null ) {
            this.window = new CEPWindow();
        }
        return this.window;
    }

    public String getEntryPointName() {
        return entryPointName;
    }

    public void setEntryPointName(String entryPointName) {
        this.entryPointName = entryPointName;
    }

    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) {
            return false;
        }
        if ( !(obj instanceof ConditionCol) ) {
            return false;
        }
        ConditionCol that = (ConditionCol) obj;
        return nullOrEqual( this.header,
                            that.header )
                && nullOrEqual( this.factType,
                                that.factType )
                && nullOrEqual( this.boundName,
                                that.boundName )
                && this.constraintValueType == that.constraintValueType
                && nullOrEqual( this.factField,
                                that.factField )
                && nullOrEqual( this.fieldType,
                                that.fieldType )
                && nullOrEqual( this.operator,
                                that.operator )
                && nullOrEqual( this.valueList,
                                that.valueList )
                && this.isNegated == that.isNegated
                && nullOrEqual( this.parameters,
                                that.parameters )
                && nullOrEqual( this.window,
                                that.window )
                && super.equals( obj );
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + (header == null ? 0 : header.hashCode());
        hash = hash * 31 + (factType == null ? 0 : factType.hashCode());
        hash = hash * 31 + (boundName == null ? 0 : boundName.hashCode());
        hash = hash * 31 + constraintValueType;
        hash = hash * 31 + (factField == null ? 0 : factField.hashCode());
        hash = hash * 31 + (fieldType == null ? 0 : fieldType.hashCode());
        hash = hash * 31 + (operator == null ? 0 : operator.hashCode());
        hash = hash * 31 + (valueList == null ? 0 : valueList.hashCode());
        hash = hash * 31 + (new Boolean( isNegated ).hashCode());
        hash = hash * 31 + parameters.hashCode();
        hash = hash * 31 + window.hashCode();
        hash = hash * 31 + super.hashCode();
        return hash;
    }

    private boolean nullOrEqual(Object thisAttr,
                                Object thatAttr) {
        if ( thisAttr == null && thatAttr == null ) {
            return true;
        }
        if ( thisAttr == null && thatAttr != null ) {
            return false;
        }
        return thisAttr.equals( thatAttr );
    }

}
