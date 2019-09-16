function showNoty(type, text) {
    new Noty({
        text: text,
        type: type,
        theme: 'relax',
        timeout: 4000,
        layout: 'topRight'
    }).show();
}