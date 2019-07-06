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

import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;


/**
 * A class for encoding states in a bidirectional map and obtaining the data.
 *
 * Input is capitalized, trimmed and searched. States should be full names.
 *
 * Also consider:
 *
 *     stateMap.put("ARMED FORCES AFRICA", "AE");
 *     stateMap.put("ARMED FORCES AMERCIAS", "AA");
 *     stateMap.put("ARMED FORCES CANADA", "AE");
 *     stateMap.put("ARMED FORCES EUROPE", "AE");
 *     stateMap.put("ARMED FORCES MIDDLE EAST", "AE");
 *     stateMap.put("ARMED FORCES PACIFIC", "AP");
 *     stateMap.put("AIR FORCE", "APO");
 *     stateMap.put("NAVY", "FPO");
 *     stateMap.put("EMBASSY", "DPO");
 *
 *
 * To ensure optimal output, replace multiple spaces with a single space.
 */
public class StateEncoderData extends BaseStepData implements StepDataInterface {
  private DualHashBidiMap stateMap = new DualHashBidiMap();
  public RowMetaInterface outputRowMeta;

  /**
   * Initialize the class.
   */
  public StateEncoderData() {
    super();
    this.buildStateMap();
  }

  /**
   * Build the state map
   */
  private void buildStateMap(){
    stateMap.clear();
    stateMap.put("ALABAMA", "AL");
    stateMap.put("ALASKA", "AK");
    stateMap.put("ARIZONA", "AZ");
    stateMap.put("ARKANSAS", "AR");
    stateMap.put("CALIFORNIA", "CA");
    stateMap.put("COLORADO", "CO");
    stateMap.put("CONNECTICUT", "CT");
    stateMap.put("DELEWARE", "DE");
    stateMap.put("FLORIDA", "FL");
    stateMap.put("GEORGIA", "GA");
    stateMap.put("HAWAII", "HI");
    stateMap.put("IDAHO", "ID");
    stateMap.put("ILLINOIS", "IL");
    stateMap.put("INDIANA", "IN");
    stateMap.put("IOWA", "IA");
    stateMap.put("KANSAS", "KS");
    stateMap.put("KENTUCKY", "KY");
    stateMap.put("LOUISIANA", "LA");
    stateMap.put("MAINE", "ME");
    stateMap.put("MARYLAND", "MD");
    stateMap.put("MASSACHUSETTS", "MA");
    stateMap.put("MICHIGAN", "MI");
    stateMap.put("MINNESOTA", "MN");
    stateMap.put("MISSISSIPPI", "MS");
    stateMap.put("MISSOURI", "MO");
    stateMap.put("MONTANA", "MT");
    stateMap.put("NEBRASKA", "NE");
    stateMap.put("NEVADA", "NV");
    stateMap.put("NEW HAMPSHIRE", "NH");
    stateMap.put("NEW JERSEY", "NJ");
    stateMap.put("NEW MEXICO", "NM");
    stateMap.put("NEW YORK", "NY");
    stateMap.put("NORTH CAROLINA", "NC");
    stateMap.put("NORTH DAKOTA", "ND");
    stateMap.put("OHIO", "OH");
    stateMap.put("OKLAHOMA", "OK");
    stateMap.put("OREGON", "OR");
    stateMap.put("PENNSYLVANIA", "PA");
    stateMap.put("RHODE ISLAND", "RI");
    stateMap.put("SOUTH CAROLINA", "SC");
    stateMap.put("SOUTH DAKOTA", "SD");
    stateMap.put("TENNESSEE", "TN");
    stateMap.put("TEXAS", "TX");
    stateMap.put("UTAH", "UT");
    stateMap.put("VERMONT", "VT");
    stateMap.put("VIRGINIA", "VA");
    stateMap.put("WASHINGTON", "WA");
    stateMap.put("WEST VIRGINIA", "WV");
    stateMap.put("WISCONSIN", "WI");
    stateMap.put("WYOMING", "WY");
    stateMap.put("AMERICAN SAMOA", "AS");
    stateMap.put("DISTRICT OF COLUMBIA", "DC");
    stateMap.put("FEDERATED STATES OF MICRONESIA", "FM");
    stateMap.put("GUAM", "GU");
    stateMap.put("MARSHALL ISLANDS", "MH");
    stateMap.put("NORTH MARIANA ISLANDS", "MP");
    stateMap.put("PALAU", "PW");
    stateMap.put("PUERTO RICO", "PR");
    stateMap.put("VIRGIN ISLANDS", "VI");
    stateMap.put("ARMED FORCES AFRICA", "AE");
    stateMap.put("ARMED FORCES AMERCIAS", "AA");
    stateMap.put("ARMED FORCES CANADA", "AE");
    stateMap.put("ARMED FORCES EUROPE", "AE");
    stateMap.put("ARMED FORCES MIDDLE EAST", "AE");
    stateMap.put("ARMED FORCES PACIFIC", "AP");
    stateMap.put("AIR FORCE", "APO");
    stateMap.put("NAVY", "FPO");
    stateMap.put("EMBASSY", "DPO");
  }

  /**
   * Get a state abbreviation from the String
   *
   * @param state     The state
   * @return          The abbreviation or null
   */
  public String getStateAbbreviation(String state){
    if(this.stateMap.containsKey(state)){
      return (String) this.stateMap.get(state);
    }else{
      return null;
    }
  }

  /**
   * Get the state string
   *
   * @param abbreviation        The abbreviation
   * @return                    The state or null if not found
   */
  public String getStateString(String abbreviation){
    if(this.stateMap.containsValue(abbreviation)){
      return (String) this.stateMap.getKey(abbreviation);
    }else{
      return null;
    }
  }
}