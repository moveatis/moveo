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

/**
 * @fileOverview JavaScript methods for control view.
 */

/* global PrimeFaces, PF */

/**
 * Hide dialog on succesfull submit. Additionally, display the provided message.
 * @param {object} args - containing the validationFailed attribute.
 * @param {string} dialogWidgetVar - Dialogs widget variable.
 * @param {string} message - given message
 */
function onDialogSuccess(args, dialogWidgetVar, message) {
    if (args && !args.validationFailed) {
        PF(dialogWidgetVar).hide();
        if (message) {
            PF('growlWdgt').renderMessage({summary: message, severity: 'info'});
        }
    }
}

/**
 * Set last editable data table row into edit mode and set focus to it's first editable input.
 * @param {string} table data table's identifier, id or class name.
 */
function focusDataTableEditInput(table) {
    $(table + ' .ui-datatable-data tr').last().find('.ui-icon-pencil').each(function(){
        $(this).click();
    });
    $(table + ' .ui-datatable-data tr').last().find('.ui-editable-column:first-child input').focus();
    $(table + ' .ui-datatable-data tr').last().find('.ui-editable-column:first-child input').select();
}

/**
 * Submit data table rows in edit mode.
 * @param {string} table - data table's identifier, id or class name.
 */
function submitDataTableEditInputs(table) {
    $(table + ' .ui-datatable-data tr').find('.ui-icon-check').each(function(){
        $(this).click();
    }); 
}