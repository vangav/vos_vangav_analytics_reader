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

package com.vangav.vos_vangav_analytics_reader.controllers.get_total_action_counters;

import java.util.ArrayList;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ResultSet;
import com.vangav.backend.cassandra.Cassandra;
import com.vangav.backend.play_framework.request.Request;
import com.vangav.backend.play_framework.request.RequestJsonBody;
import com.vangav.backend.play_framework.request.response.ResponseBody;
import com.vangav.vos_vangav_analytics_reader.actions.ActionsManager;
import com.vangav.vos_vangav_analytics_reader.cassandra_keyspaces.v_analytics.TotalActionCounters;
import com.vangav.vos_vangav_analytics_reader.controllers.CommonPlayHandler;
import com.vangav.vos_vangav_analytics_reader.controllers.get_total_action_counters.response_json.ResponseTotalAction;

/**
 * GENERATED using ControllersGeneratorMain.java
 */
/**
 * HandlerGetTotalActionCounters
 *   handles request-to-response processing
 *   also handles after response processing (if any)
 * */
public class HandlerGetTotalActionCounters extends CommonPlayHandler {

  private static final String kName = "GetTotalActionCounters";

  @Override
  protected String getName () {

    return kName;
  }

  @Override
  protected RequestJsonBody getRequestJson () {

    return new RequestGetTotalActionCounters();
  }

  @Override
  protected ResponseBody getResponseBody () {

    return new ResponseGetTotalActionCounters();
  }

  @Override
  protected void processRequest (final Request request) throws Exception {

    // use the following request Object to process the request and set
    //   the response to be returned
    RequestGetTotalActionCounters requestGetTotalActionCounters =
      (RequestGetTotalActionCounters)request.getRequestJsonBody();
    
    // make select queries
    
    ArrayList<BoundStatement> boundStatements =
      new ArrayList<BoundStatement>();
    
    for (String actionId : requestGetTotalActionCounters.action_id) {
      
      boundStatements.add(
        TotalActionCounters.i().getBoundStatementSelect(
          ActionsManager.format(
            requestGetTotalActionCounters.class_prefix,
            actionId) ) );
    }
    
    // execute queies
    
    ArrayList<ResultSet> resultSets =
      Cassandra.i().executeSync(
        boundStatements.toArray(new BoundStatement[0] ) );
    
    // build response
    
    ArrayList<ResponseTotalAction> actions =
      new ArrayList<ResponseTotalAction>();
    
    for (int i = 0; i < requestGetTotalActionCounters.action_id.length; i ++) {
      
      if (resultSets.get(i).isExhausted() == true) {
        
        actions.add(
          new ResponseTotalAction(
            requestGetTotalActionCounters.action_id[i] ) );
      } else {
        
        actions.add(
          new ResponseTotalAction(
            requestGetTotalActionCounters.action_id[i],
            resultSets.get(i).one().getLong(
              TotalActionCounters.kActionCountColumnName) ) );
      }
    }
    
    // set response
    ((ResponseGetTotalActionCounters)request.getResponseBody() ).set(
      requestGetTotalActionCounters.class_prefix,
      actions.toArray(new ResponseTotalAction[0] ) );
  }
}
