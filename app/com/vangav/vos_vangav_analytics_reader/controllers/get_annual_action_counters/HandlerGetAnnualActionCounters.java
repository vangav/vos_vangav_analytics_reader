/**
 * "First, solve the problem. Then, write the code. -John Johnson"
 * "Or use Vangav M"
 * www.vangav.com
 * */

/**
 * MIT License
 *
 * Copyright (c) 2016 Vangav
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 * */

/**
 * Community
 * Facebook Group: Vangav Open Source - Backend
 *   fb.com/groups/575834775932682/
 * Facebook Page: Vangav
 *   fb.com/vangav.f
 * 
 * Third party communities for Vangav Backend
 *   - play framework
 *   - cassandra
 *   - datastax
 *   
 * Tag your question online (e.g.: stack overflow, etc ...) with
 *   #vangav_backend
 *   to easier find questions/answers online
 * */

package com.vangav.vos_vangav_analytics_reader.controllers.get_annual_action_counters;

import java.util.ArrayList;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ResultSet;
import com.vangav.backend.cassandra.Cassandra;
import com.vangav.backend.play_framework.request.Request;
import com.vangav.backend.play_framework.request.RequestJsonBody;
import com.vangav.backend.play_framework.request.response.ResponseBody;
import com.vangav.vos_vangav_analytics_reader.actions.ActionsManager;
import com.vangav.vos_vangav_analytics_reader.cassandra_keyspaces.v_analytics.AnnualActionCounters;
import com.vangav.vos_vangav_analytics_reader.controllers.CommonPlayHandler;
import com.vangav.vos_vangav_analytics_reader.controllers.get_annual_action_counters.response_json.ResponseYearAction;
import com.vangav.vos_vangav_analytics_reader.controllers.get_annual_action_counters.response_json.ResponseYearsAction;

/**
 * GENERATED using ControllersGeneratorMain.java
 */
/**
 * HandlerGetAnnualActionCounters
 *   handles request-to-response processing
 *   also handles after response processing (if any)
 * */
public class HandlerGetAnnualActionCounters extends CommonPlayHandler {

  private static final String kName = "GetAnnualActionCounters";

  @Override
  protected String getName () {

    return kName;
  }

  @Override
  protected RequestJsonBody getRequestJson () {

    return new RequestGetAnnualActionCounters();
  }

  @Override
  protected ResponseBody getResponseBody () {

    return new ResponseGetAnnualActionCounters();
  }

  @Override
  protected void processRequest (final Request request) throws Exception {

    // use the following request Object to process the request and set
    //   the response to be returned
    RequestGetAnnualActionCounters requestGetAnnualActionCounters =
      (RequestGetAnnualActionCounters)request.getRequestJsonBody();
    
    // set to-year
    
    int toYear = requestGetAnnualActionCounters.from_year;
    
    if (requestGetAnnualActionCounters.isValidParam(
          RequestGetAnnualActionCounters.kToYearName) == true) {
      
      toYear = requestGetAnnualActionCounters.to_year;
    }
    
    // build response
    
    ArrayList<BoundStatement> boundStatements;
    ArrayList<ResultSet> resultSets;
    ArrayList<ResponseYearAction> currYears;
    int index;
    ArrayList<ResponseYearsAction> actions =
      new ArrayList<ResponseYearsAction>();
    
    // for each action
    for (String actionId : requestGetAnnualActionCounters.action_id) {
      
      boundStatements = new ArrayList<BoundStatement>();
      
      // for each year
      for (int year = requestGetAnnualActionCounters.from_year;
           year <= toYear;
          year ++) {
        
        // make select query
        boundStatements.add(
          AnnualActionCounters.i().getBoundStatementSelect(
            year
              + "_"
              + ActionsManager.format(
                  requestGetAnnualActionCounters.class_prefix,
                  actionId) ) );
      }
      
      // execute years queries for the current action
      resultSets =
        Cassandra.i().executeSync(
        boundStatements.toArray(new BoundStatement[0] ) );

      currYears = new ArrayList<ResponseYearAction>();
      index = 0;
      
      // extract count from result sets for each year for the current action
      for (int year = requestGetAnnualActionCounters.from_year;
           year <= toYear;
          year ++, index ++) {
        
        if (resultSets.get(index).isExhausted() == true) {
          
          currYears.add(new ResponseYearAction(year) );
        } else {
          
          currYears.add(
            new ResponseYearAction(
              toYear,
              resultSets.get(index).one().getLong(
                AnnualActionCounters.kActionCountColumnName) ) );
        }
      }
      
      // store the current action's annual counts
      actions.add(
        new ResponseYearsAction(
          actionId,
          currYears.toArray(new ResponseYearAction[0] ) ) );
    } // for (String actionId : requestGetAnnualActionCounters.action_id) {
    
    // set response
    ((ResponseGetAnnualActionCounters)request.getResponseBody() ).set(
      requestGetAnnualActionCounters.class_prefix,
      actions.toArray(new ResponseYearsAction[0] ) );
  }
}
