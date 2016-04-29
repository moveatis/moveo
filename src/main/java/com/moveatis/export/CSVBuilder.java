/* 
 * Copyright (c) 2016, Jarmo Juujärvi, Sami Kallio, Kai Korhonen, Juha Moisio, Ilari Paananen 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 *     3. Neither the name of the copyright holder nor the names of its 
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.moveatis.export;

/**
 *
 * @author ilkrpaan
 */
public class CSVBuilder {
    private StringBuilder sb;
    private String sep;
    private boolean atLineBegin;

    public CSVBuilder(String separator) {
        sb = new StringBuilder();
        sep = separator;
        atLineBegin = true;
    }

    private void addSep() {
        if (atLineBegin)
            atLineBegin = false;
        else
            sb.append(sep);
    }

    public CSVBuilder add(long value) {
        addSep();
        sb.append(value);
        return this;
    }
    
    public CSVBuilder addPercent(long value) {
        add(value);
        sb.append("%");
        return this;
    }

    public CSVBuilder add(String value) {
        addSep();
        sb.append('"');
        if (value != null)
            sb.append(value.replace("\"", "\"\""));
        sb.append('"');
        return this;
    }

    public CSVBuilder newLine() {
        sb.append("\n"); // TODO: System.lineSeparator() or user's system line separator?
        atLineBegin = true;
        return this;
    }

    public String getCSV() {
        return sb.toString();
    }
}
