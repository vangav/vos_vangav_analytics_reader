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

package com.vangav.vos_vangav_analytics_reader.controllers.get_monthly_action_counters;

import java.util.ArrayList;
import java.util.Calendar;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ResultSet;
import com.vangav.backend.cassandra.Cassandra;
import com.vangav.backend.cassandra.formatting.CalendarFormatterInl;
import com.vangav.backend.metrics.time.CalendarAndDateOperationsInl;
import com.vangav.backend.play_framework.param.ParamParsersInl;
import com.vangav.backend.play_framework.request.Request;
import com.vangav.backend.play_framework.request.RequestJsonBody;
import com.vangav.backend.play_framework.request.response.ResponseBody;
import com.vangav.vos_vangav_analytics_reader.actions.ActionsManager;
import com.vangav.vos_vangav_analytics_reader.cassandra_keyspaces.v_analytics.MonthlyActionCounters;
import com.vangav.vos_vangav_analytics_reader.controllers.CommonPlayHandler;
import com.vangav.vos_vangav_analytics_reader.controllers.get_monthly_action_counters.response_json.ResponseMonthAction;
import com.vangav.vos_vangav_analytics_reader.controllers.get_monthly_action_counters.response_json.ResponseMonthsAction;

/**
 * GENERATED using ControllersGeneratorMain.java
 */
/**
 * HandlerGetMonthlyActionCounters
 *   handles request-to-response processing
 *   also handles after response processing (if any)
 * */
public class HandlerGetMonthlyActionCounters extends CommonPlayHandler {

  private static final String kName = "GetMonthlyActionCounters";

  @Override
  protected String getName () {

    return kName;
  }

  @Override
  protected RequestJsonBody getRequestJson () {

    return new RequestGetMonthlyActionCounters();
  }

  @Override
  protected ResponseBody getResponseBody () {

    return new ResponseGetMonthlyActionCounters();
  }

  @Override
  protected void processRequest (final Request request) throws Exception {

    // use the following request Object to process the request and set
    //   the response to be returned
    RequestGetMonthlyActionCounters requestGetMonthlyActionCounters =
      (RequestGetMonthlyActionCounters)request.getRequestJsonBody();
    
    // set months' dates range
    
    Calendar fromCalendar =
      ParamParsersInl.parseCalendar(requestGetMonthlyActionCounters.from_date);
    Calendar toCalendar =
      ParamParsersInl.parseCalendar(requestGetMonthlyActionCounters.from_date);
    
    if (requestGetMonthlyActionCounters.isValidParam(
          RequestGetMonthlyActionCounters.kToDateName) == true) {
      
      toCalendar =
        ParamParsersInl.parseCalendar(requestGetMonthlyActionCounters.to_date);
    }
    
    ArrayList<Calendar> calendarRange =
      CalendarAndDateOperationsInl.getCalendarMonthsFromTo(
        fromCalendar,
        toCalendar);
    
    // build response
    
    ArrayList<BoundStatement> boundStatements;
    ArrayList<ResultSet> resultSets;
    ArrayList<ResponseMonthAction> currMonths;
    ArrayList<ResponseMonthsAction> actions =
      new ArrayList<ResponseMonthsAction>();
    
    // for each action
    for (String actionId : requestGetMonthlyActionCounters.action_id) {
      
      boundStatements = new ArrayList<BoundStatement>();
      
      // for each month
      for (Calendar month : calendarRange) {
        
        // make select query
        boundStatements.add(
          MonthlyActionCounters.i().getBoundStatementSelect(
            CalendarFormatterInl.concatCalendarFields(
              month,
              Calendar.YEAR,
              Calendar.MONTH)
            + "_"
            + ActionsManager.format(
                requestGetMonthlyActionCounters.class_prefix,
                actionId) ) );
      }
      
      // execute days queries for the current action
      resultSets =
        Cassandra.i().executeSync(
        boundStatements.toArray(new BoundStatement[0] ) );
      
      currMonths = new ArrayList<ResponseMonthAction>();
      
      // extract count from result sets for each month for the current action
      for (int i = 0; i < calendarRange.size(); i ++) {
        
        if (resultSets.get(i).isExhausted() == true) {
          
          currMonths.add(new ResponseMonthAction(calendarRange.get(i) ) );
        } else {
          
          currMonths.add(
            new ResponseMonthAction(
              calendarRange.get(i),
              resultSets.get(i).one().getLong(
                MonthlyActionCounters.kActionCountColumnName) ) );
        }
      }
      
      // store the current action's monthly counts
      actions.add(
        new ResponseMonthsAction(
          actionId,
          currMonths.toArray(new ResponseMonthAction[0] ) ) );
    } // for (String actionId : requestGetMonthlyActionCounters.action_id)

    // set response
    ((ResponseGetMonthlyActionCounters)request.getResponseBody() ).set(
      requestGetMonthlyActionCounters.class_prefix,
      actions.toArray(new ResponseMonthsAction[0] ) );
  }
}
