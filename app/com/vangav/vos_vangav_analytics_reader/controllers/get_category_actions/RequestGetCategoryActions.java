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

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vangav.backend.exceptions.BadRequestException;
import com.vangav.backend.exceptions.VangavException.ExceptionClass;
import com.vangav.backend.play_framework.param.ParamOptionality;
import com.vangav.backend.play_framework.param.ParamType;
import com.vangav.backend.play_framework.request.RequestJsonBodyGet;
import com.vangav.vos_vangav_analytics_reader.actions.ActionsManager;

/**
 * GENERATED using ControllersGeneratorMain.java
 */
/**
 * RequestGetCategoryActions represents the request's structure
 * */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestGetCategoryActions extends RequestJsonBodyGet {

  @Override
  @JsonIgnore
  protected String getName () throws Exception {

    return "GetCategoryActions";
  }

  @Override
  @JsonIgnore
  protected RequestGetCategoryActions getThis () throws Exception {

    return this;
  }

  @JsonIgnore
  public static final String kCateogryNameShortName = "category_name_short";
  @JsonProperty
  public String category_name_short;

  @Override
  @JsonIgnore
  public RequestGetCategoryActions fromQueryString (
    Map<String, String[]> query) throws Exception {

    this.category_name_short = this.getString(kCateogryNameShortName, query);
    
    return this;
  }

  @Override
  @JsonIgnore
  public void validate () throws Exception {

    this.validate(
      kCateogryNameShortName,
      this.category_name_short,
      ParamType.ALPHA_NUMERIC_DASH_UNDERSCORE,
      ParamOptionality.MANDATORY);

    // validate that this category-name-short exists
    if (ActionsManager.i().validateCategory(
          this.category_name_short) == false) {
      
      throw new BadRequestException(
        507,
        1,
        "category-name-short ["
          + this.category_name_short
          + "] doesn't exist",
        ExceptionClass.INVALID);
    }
  }
}
