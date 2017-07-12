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
 * RequestGetAnnualActionCounters represents the request's structure
 * */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestGetAnnualActionCounters extends RequestJsonBodyGet {

  @Override
  @JsonIgnore
  protected String getName () throws Exception {

    return "GetAnnualActionCounters";
  }

  @Override
  @JsonIgnore
  protected RequestGetAnnualActionCounters getThis () throws Exception {

    return this;
  }

  @JsonIgnore
  public static final String kClassPrefixName = "class_prefix";
  @JsonProperty
  public String class_prefix;

  @JsonIgnore
  public static final String kActionIdName = "action_id";
  @JsonProperty
  public String[] action_id;

  @JsonIgnore
  public static final String kFromYearName = "from_year";
  @JsonProperty
  public int from_year;

  @JsonIgnore
  public static final String kToYearName = "to_year";
  @JsonProperty
  public int to_year;

  @Override
  @JsonIgnore
  public RequestGetAnnualActionCounters fromQueryString (
    Map<String, String[]> query) throws Exception {

    this.class_prefix = this.getString(kClassPrefixName, query);
    this.action_id = this.getStringArray(kActionIdName, query);
    this.from_year = this.getInt(kFromYearName, query);
    this.to_year = this.getInt(kToYearName, query);

    return this;
  }

  @Override
  @JsonIgnore
  public void validate () throws Exception {

    this.validate(
      kClassPrefixName,
      this.class_prefix,
      ParamType.ALPHA_NUMERIC_DASH_UNDERSCORE,
      ParamOptionality.MANDATORY);

    this.validate(
      kActionIdName,
      this.action_id,
      ParamType.ALPHA_NUMERIC_DASH_UNDERSCORE,
      ParamOptionality.MANDATORY);

    this.validate(
      kFromYearName,
      this.from_year,
      ParamType.INT,
      ParamOptionality.MANDATORY);

    this.validate(
      kToYearName,
      this.to_year,
      ParamType.INT,
      ParamOptionality.OPTIONAL);

    // more validation
    
    // validate actions class prefix
    if (ActionsManager.i().validateActionClass(
          this.class_prefix) == false) {
      
      throw new BadRequestException(
        501,
        1,
        "Invalid actions class prefix ["
          + this.class_prefix
          + "]",
        ExceptionClass.INVALID);
    }
    
    // validate actions
    for (String actionId : this.action_id) {
      
      if (ActionsManager.i().validateActionId(
            this.class_prefix,
            actionId) == false) {
        
        throw new BadRequestException(
          501,
          2,
          "Invalid action id ["
            + this.action_id
            + "] doesn't belong to actions class prefix ["
            + this.class_prefix
            + "]",
          ExceptionClass.INVALID);
      }
    }
    
    // validate years
    if (this.isValidParam(kToYearName) == true) {
      
      if (this.to_year < this.from_year) {
        
        throw new BadRequestException(
          501,
          3,
          "to year ["
            + this.to_year
            + "] can't be smaller than from year ["
            + this.from_year
            + "]",
          ExceptionClass.INVALID);
      }
      
      if ((this.to_year - this.from_year) > 100) {
        
        throw new BadRequestException(
          501,
          4,
          "the from year ["
           + this.from_year
           + "] to year ["
           + this.to_year
           + "] can't be greater than 100 per request, current range is ["
           + (this.to_year - this.from_year)
           + "]",
          ExceptionClass.INVALID);
      }
    }
  }
}
