/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.si;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaBoolean;
import org.pentaho.di.core.row.value.ValueMetaFactory;
import org.pentaho.di.core.row.value.ValueMetaString;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Skeleton for PDI Step plugin.
 */
@Step( id = "StateEncoder", image = "StateEncoder.svg", name = "Encode State",
    description = "Encodes a state string to an abbreviation.", categoryDescription = "Transform" )
public class StateEncoderMeta extends BaseStepMeta implements StepMetaInterface {
  
  private static Class<?> PKG = StateEncoder.class; // for i18n purposes, needed by Translator2!!   $NON-NLS-1$
  private boolean reverse = false;
  private String inField = "";
  private String outField = "";

  public StateEncoderMeta() {
    super(); // allocate BaseStepMeta
  }

  public boolean isReverse() {
    return reverse;
  }

  public void setReverse(boolean reverse) {
    this.reverse = reverse;
  }

  public String getInField() {
    return inField;
  }

  public void setInField(String inField) {
    this.inField = inField;
  }

  public String getOutField() {
    return outField;
  }

  public void setOutField(String outField) {
    this.outField = outField;
  }

  public String getXML() throws KettleValueException {
    StringBuilder xml = new StringBuilder();
    xml.append(XMLHandler.addTagValue("inField", inField));
    xml.append(XMLHandler.addTagValue("outField", outField));
    return xml.toString();
  }

  public void loadXML(Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
    readData( stepnode );
  }

  public Object clone() {
    Object retval = super.clone();
    return retval;
  }
  
  private void readData( Node stepnode ) throws KettleXMLException {
    try {
      setInField(Const.NVL(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "inField")), ""));
      setOutField(Const.NVL(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "outField")), ""));
      setReverse(Const.NVL(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "reverse")), "N").equals("Y"));
    } catch ( Exception e ) {
      throw new KettleXMLException( "Demo plugin unable to read step info from XML node", e );
    }
  }

  public void setDefault() {
    this.inField = "";
    this.outField = "";
    this.reverse = false;
  }

  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases ) throws KettleException {
    this.inField = rep.getStepAttributeString(id_step, "inField");
    this.outField = rep.getStepAttributeString(id_step, "outField");
    this.reverse = rep.getStepAttributeBoolean(id_step, "reverse");
  }
  
  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step )
    throws KettleException {
    rep.saveStepAttribute(id_transformation, id_step, "inField", inField);
    rep.saveStepAttribute(id_transformation, id_step, "outField", outField);
    rep.saveStepAttribute(id_transformation, id_step, "reverse", reverse);

  }
  
  public void getFields( RowMetaInterface rowMeta, String origin, RowMetaInterface[] info, StepMeta nextStep, 
    VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {
    if(!Arrays.asList(rowMeta.getFieldNames()).contains(outField)) {
      ValueMetaInterface v0 = new ValueMetaString(outField);
      v0.setTrimType(ValueMetaInterface.TRIM_TYPE_BOTH);
      v0.setOrigin(outField);
      rowMeta.addValueMeta(v0);
    }
  }
  
  public void check( List<CheckResultInterface> remarks, TransMeta transMeta, 
    StepMeta stepMeta, RowMetaInterface prev, String input[], String output[],
    RowMetaInterface info, VariableSpace space, Repository repository, 
    IMetaStore metaStore ) {
    CheckResult cr;
    if ( prev == null || prev.size() == 0 ) {
      cr = new CheckResult( CheckResultInterface.TYPE_RESULT_WARNING, BaseMessages.getString( PKG, "StateEncoderMeta.CheckResult.NotReceivingFields" ), stepMeta ); 
      remarks.add( cr );
    }
    else {
      cr = new CheckResult( CheckResultInterface.TYPE_RESULT_OK, BaseMessages.getString( PKG, "StateEncoderMeta.CheckResult.StepRecevingData", prev.size() + "" ), stepMeta );  
      remarks.add( cr );
    }
    
    // See if we have input streams leading to this step!
    if ( input.length > 0 ) {
      cr = new CheckResult( CheckResultInterface.TYPE_RESULT_OK, BaseMessages.getString( PKG, "StateEncoderMeta.CheckResult.StepRecevingData2" ), stepMeta ); 
      remarks.add( cr );
    }
    else {
      cr = new CheckResult( CheckResultInterface.TYPE_RESULT_ERROR, BaseMessages.getString( PKG, "StateEncoderMeta.CheckResult.NoInputReceivedFromOtherSteps" ), stepMeta ); 
      remarks.add( cr );
    }
  }
  
  public StepInterface getStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta tr, Trans trans ) {
    return new StateEncoder( stepMeta, stepDataInterface, cnr, tr, trans );
  }
  
  public StepDataInterface getStepData() {
    return new StateEncoderData();
  }

  public String getDialogClassName() {
    return "com.si.StateEncoderDialog";
  }
}
