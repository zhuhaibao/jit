/*!
 * Jodit Editor PRO (https://xdsoft.net/jodit/)
 * See LICENSE.MD in the project root for license information.
 * Copyright (c) 2013-2020 Valeriy Chupurnov. All rights reserved. https://xdsoft.net/jodit/pro/
 */

Jodit.defaultOptions.license = '5BA1C-3KLI3-BDPK6-J4EPY';
Jodit.defaultOptions.language = 'zh_cn';

Jodit.defaultOptions.width = 'auto';
Jodit.defaultOptions.height = 'auto';
Jodit.defaultOptions.minHeight = 500;
Jodit.defaultOptions.theme = 'default';
Jodit.defaultOptions.colorPickerDefaultTab = 'background';
Jodit.defaultOptions.defaultMode = Jodit.MODE_WYSIWYG;
Jodit.defaultOptions.toolbarButtonSize = 'middle';

Jodit.defaultOptions.spellcheck = true;
Jodit.defaultOptions.i18n = 'en';
Jodit.defaultOptions.tabIndex = -1;
Jodit.defaultOptions.activeButtonsInReadOnly = ['source', 'fullsize', 'preview', 'print', 'about', 'dots'];
Jodit.defaultOptions.zIndex = 0;
Jodit.defaultOptions.readonly = false;


Jodit.defaultOptions.events = {};
Jodit.defaultOptions.textIcons = false;


Jodit.defaultOptions.extraPlugins = [
    'autocomplete',
    'emoji',
    'google-search',
    'pasteCode',
    'color-picker',
];
Jodit.defaultOptions.buttons = [
    'source', '|',
    'bold',
    'strikethrough',
    'underline',
    'italic', '|',
    'ul',
    'ol', '|',
    'outdent', 'indent', 'align', 'classSpan', 'superscript', 'subscript', '|',
    'pasteCode', '|', //代码编辑
    'font',
    'fontsize',
    'lineHeight',
    'symbols',
    'emoji', //表情包
    'brush',
    'paragraph', '|',
    'image',
    'video',
    'table',
    'link', '|',
    'undo', 'redo', '|',
    'hr',
    'eraser',
    'copyformat', '|',
    'find',
    'google', //google搜索
    'preview',
    'fullsize',
    'print',
    'about',
];
Jodit.defaultOptions.buttonsXS = [
    'bold',
    'image', '|',
    'brush',
    'paragraph', '|',
    'align', '|',
    'undo', 'redo', '|',
    'eraser',
    'dots'
];

Jodit.defaultOptions.disablePlugins = [];
Jodit.defaultOptions.removeButtons = [];