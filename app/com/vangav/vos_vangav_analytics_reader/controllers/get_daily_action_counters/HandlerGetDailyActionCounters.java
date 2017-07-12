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

package com.vangav.vos_vangav_analytics_reader.controllers.get_daily_action_counters;

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
import com.vangav.vos_vangav_analytics_reader.cassandra_keyspaces.v_analytics.DailyActionCounters;
import com.vangav.vos_vangav_analytics_reader.controllers.CommonPlayHandler;
import com.vangav.vos_vangav_analytics_reader.controllers.get_daily_action_counters.response_json.ResponseDayAction;
import com.vangav.vos_vangav_analytics_reader.controllers.get_daily_action_counters.response_json.ResponseDaysAction;

/**
 * GENERATED using ControllersGeneratorMain.java
 */
/**
 * HandlerGetDailyActionCounters
 *   handles request-to-response processing
 *   also handles after response processing (if any)
 * */
public class HandlerGetDailyActionCounters extends CommonPlayHandler {

  private static final String kName = "GetDailyActionCounters";

  @Override
  protected String getName () {

    return kName;
  }

  @Override
  protected RequestJsonBody getRequestJson () {

    return new RequestGetDailyActionCounters();
  }

  @Override
  protected ResponseBody getResponseBody () {

    return new ResponseGetDailyActionCounters();
  }

  @Override
  protected void processRequest (final Request request) throws Exception {

    // use the following request Object to process the request and set
    //   the response to be returned
    RequestGetDailyActionCounters requestGetDailyActionCounters =
      (RequestGetDailyActionCounters)request.getRequestJsonBody();
    
    // set dates range
    
    Calendar fromCalendar =
      ParamParsersInl.parseCalendar(requestGetDailyActionCounters.from_date);
    Calendar toCalendar =
      ParamParsersInl.parseCalendar(requestGetDailyActionCounters.from_date);
    
    if (requestGetDailyActionCounters.isValidParam(
          RequestGetDailyActionCounters.kToDateName) == true) {
      
      toCalendar =
        ParamParsersInl.parseCalendar(requestGetDailyActionCounters.to_date);
    }
    
    ArrayList<Calendar> calendarRange =
      CalendarAndDateOperationsInl.getCalendarsFromTo(
        fromCalendar,
        toCalendar);
    
    // build response
    
    ArrayList<BoundStatement> boundStatements;
    ArrayList<ResultSet> resultSets;
    ArrayList<ResponseDayAction> currDays;
    ArrayList<ResponseDaysAction> actions =
      new ArrayList<ResponseDaysAction>();
    
    // for each action
    for (String actionId : requestGetDailyActionCounters.action_id) {
      
      boundStatements = new ArrayList<BoundStatement>();
      
      // for each day
      for (Calendar day : calendarRange) {
        
        // make select query
        boundStatements.add(
          DailyActionCounters.i().getBoundStatementSelect(
            CalendarFormatterInl.concatCalendarFields(
              day,
              Calendar.YEAR,
              Calendar.MONTH,
              Calendar.DAY_OF_MONTH)
            + "_"
            + ActionsManager.format(
                requestGetDailyActionCounters.class_prefix,
                actionId) ) );
      }
      
      // execute days queries for the current action
      resultSets =
        Cassandra.i().executeSync(
        boundStatements.toArray(new BoundStatement[0] ) );
      
      currDays = new ArrayList<ResponseDayAction>();
      
      // extract count from result sets for each day for the current action
      for (int i = 0; i < calendarRange.size(); i ++) {
        
        if (resultSets.get(i).isExhausted() == true) {
          
          currDays.add(new ResponseDayAction(calendarRange.get(i) ) );
        } else {
          
          currDays.add(new ResponseDayAction(
            calendarRange.get(i),
            resultSets.get(i).one().getLong(
              DailyActionCounters.kActionCountColumnName) ) );
        }
      }
      
      // store the current action's daily counts
      actions.add(
        new ResponseDaysAction(
          actionId,
          currDays.toArray(new ResponseDayAction[0] ) ) );
    } // for (String actionId : requestGetDailyActionCounters.action_id)
    
    // set response
    ((ResponseGetDailyActionCounters)request.getResponseBody() ).set(
      requestGetDailyActionCounters.class_prefix,
      actions.toArray(new ResponseDaysAction[0] ) );
  }
}
