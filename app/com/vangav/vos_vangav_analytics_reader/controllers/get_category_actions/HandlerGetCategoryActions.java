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

package com.vangav.vos_vangav_analytics_reader.controllers.get_category_actions;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.vangav.backend.play_framework.request.Request;
import com.vangav.backend.play_framework.request.RequestJsonBody;
import com.vangav.backend.play_framework.request.response.ResponseBody;
import com.vangav.vos_vangav_analytics_reader.actions.ActionsManager;
import com.vangav.vos_vangav_analytics_reader.controllers.CommonPlayHandler;
import com.vangav.vos_vangav_analytics_reader.controllers.get_category_actions.response_json.ResponseClassActions;

/**
 * GENERATED using ControllersGeneratorMain.java
 */
/**
 * HandlerGetCategoryActions
 *   handles request-to-response processing
 *   also handles after response processing (if any)
 * */
public class HandlerGetCategoryActions extends CommonPlayHandler {

  private static final String kName = "GetCategoryActions";

  @Override
  protected String getName () {

    return kName;
  }

  @Override
  protected RequestJsonBody getRequestJson () {

    return new RequestGetCategoryActions();
  }

  @Override
  protected ResponseBody getResponseBody () {

    return new ResponseGetCategoryActions();
  }

  @Override
  protected void processRequest (final Request request) throws Exception {

    // use the following request Object to process the request and set
    //   the response to be returned
    RequestGetCategoryActions requestGetCategoryActions =
      (RequestGetCategoryActions)request.getRequestJsonBody();
    
    // get category's actions
    Map<String, Set<String> > actionClasses =
      ActionsManager.i().getCategoryActions(
        requestGetCategoryActions.category_name_short);
    
    // build response
    
    ArrayList<ResponseClassActions> actionsClasses =
      new ArrayList<ResponseClassActions>();
    
    for (String actionsClassPrefix : actionClasses.keySet() ) {
      
      actionsClasses.add(
        new ResponseClassActions(
          actionsClassPrefix,
          actionClasses.get(actionsClassPrefix).toArray(new String[0] ) ) );
    }
    
    // set response
    ((ResponseGetCategoryActions)request.getResponseBody() ).set(
      requestGetCategoryActions.category_name_short,
      actionsClasses.toArray(new ResponseClassActions[0] ) );
  }
}
