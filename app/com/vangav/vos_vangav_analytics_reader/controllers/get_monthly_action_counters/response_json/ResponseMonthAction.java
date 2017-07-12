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

package com.vangav.vos_vangav_analytics_reader.controllers.get_monthly_action_counters.response_json;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author mustapha
 * fb.com/mustapha.abdallah
 */
/**
 * ResponseMonthAction represents a month's count of an action
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseMonthAction {

  @JsonProperty
  public String month;
  @JsonProperty
  public long count;
  
  @JsonIgnore
  private static final String kInvalidMonth = "invalid_month";
  
  /**
   * Constructor - ResponseMonthAction
   * @param calendar
   * @return new ResponseMonthAction default Object with count = 0
   * @throws Exception
   */
  @JsonIgnore
  public ResponseMonthAction (
    Calendar calendar) throws Exception {
    
    this(calendar, 0L);
  }
  
  /**
   * Constructor - ResponseMonthAction
   * @param calendar
   * @param count
   * @return new ResponseMonthAction Object
   * @throws Exception
   */
  @JsonIgnore
  public ResponseMonthAction (
    Calendar calendar,
    long count) throws Exception {
    
    if (calendar == null ||
        calendar.get(Calendar.MONTH) < 0 ||
        calendar.get(Calendar.MONTH) > 11) {
      
      this.month = kInvalidMonth;
    } else {
      
      this.month =
        new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH) ];
    }
    
    this.count = count;
  }
}
