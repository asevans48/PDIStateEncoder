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

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

/**
 * Describe your step plugin.
 * 
 */
public class StateEncoder extends BaseStep implements StepInterface {
  
  private static Class<?> PKG = StateEncoderMeta.class; // for i18n purposes, needed by Translator2!!   $NON-NLS-1$
  private StateEncoderData data = null;
  private StateEncoderMeta meta = null;


  public StateEncoder( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta, Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }
  
  /**
  * Initialize and do work where other steps need to wait for...
  *
  * @param stepMetaInterface    The metadata to work with
  * @param stepDataInterface    The data to initialize
  */
  public boolean init( StepMetaInterface stepMetaInterface, StepDataInterface stepDataInterface ) {
    meta = (StateEncoderMeta) stepMetaInterface;
    data = (StateEncoderData) stepDataInterface;
    return super.init( stepMetaInterface, stepDataInterface);
  }

  /**
   * Encode or decode the state
   *
   * @param row       The input row
   * @return          The updated row
   */
  private Object[] encodeOrDecodeState(Object[] row){
    String input = null;
    String output = null;
    Object[] outRow = row.clone();
    if(row.length < data.outputRowMeta.size()){
      outRow = RowDataUtil.resizeArray(row, data.outputRowMeta.size());
    }
    int idx = data.outputRowMeta.indexOfValue(meta.getInField());
    if(idx >= 0){
      input = (String) outRow[idx];
      if(input != null) {
        input = input.trim().toUpperCase();
        if(input.length() > 0) {
          if (meta.isReverse()) {
            output = data.getStateString(input);
          } else {
            output = data.getStateAbbreviation(input);
          }
          idx = data.outputRowMeta.indexOfValue(meta.getOutField());
          if (idx >= 0) {
            outRow[idx] = output;
          } else {
            if (isBasic()) {
              logBasic("Failed to Find Output Field in State Encoder");
            }
          }
        }
      }
    }else{
      if(isBasic()){
        logBasic("Failed to find Input Field for State Encoder");
      }
    }

    return outRow;
  }

  /**
   * Setup the processor.
   *
   * @throws KettleException
   */
  private void setupProcessor() throws KettleException{
    RowMetaInterface inMeta = getInputRowMeta().clone();
    data.outputRowMeta = inMeta;
    meta.getFields(data.outputRowMeta, getStepname(), null, null, this, null, null);
    //data.outputRowMeta = processRowMeta(data.outputRowMeta);
    first = false;
  }

  /**
   * Process the row. Inherited from interface.
   *
   * @param smi
   * @param sdi
   * @return
   * @throws KettleException
   */
  public boolean processRow( StepMetaInterface smi, StepDataInterface sdi ) throws KettleException {
    Object[] r = getRow();

    if(first){
      setupProcessor();
      first = false;
    }

    if ( r == null ) {
      setOutputDone();
      return false;
    }

    Object[] outRow = this.encodeOrDecodeState(r);
    putRow(data.outputRowMeta, r );


    if ( checkFeedback( getLinesRead() ) ) {
      if ( log.isBasic() )
        logBasic( BaseMessages.getString( PKG, "StateEncoder.Log.LineNumber" ) + getLinesRead() );
    }
      
    return true;
  }
}