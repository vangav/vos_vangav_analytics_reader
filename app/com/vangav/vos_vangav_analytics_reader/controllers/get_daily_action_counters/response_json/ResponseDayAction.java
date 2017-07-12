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

package com.vangav.vos_vangav_analytics_reader.controllers.get_daily_action_counters.response_json;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vangav.backend.metrics.time.CalendarAndDateOperationsInl;

/**
 * @author mustapha
 * fb.com/mustapha.abdallah
 */
/**
 * ResponseDayAction represents a day's count of an action
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDayAction {

  @JsonProperty
  public String day;
  @JsonProperty
  public long count;
  
  @JsonIgnore
  private static final String kInvalidDay = "invalid_day";
  
  /**
   * Constructor - ResponseDayAction
   * @param calendar
   * @return new ResponseDayAction default Object with count = 0
   * @throws Exception
   */
  @JsonIgnore
  public ResponseDayAction (
    Calendar calendar) throws Exception {
    
    this(calendar, 0L);
  }
  
  /**
   * Constructor - ResponseDayAction
   * @param calendar
   * @param count
   * @return new ResponseDayAction Object
   * @throws Exception
   */
  @JsonIgnore
  public ResponseDayAction (
    Calendar calendar,
    long count) throws Exception {
    
    if (calendar == null) {
      
      this.day = kInvalidDay;
    } else {
      
      this.day = CalendarAndDateOperationsInl.getFormattedDate(calendar);
    }
    
    this.count = count;
  }
}
