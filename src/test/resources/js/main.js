requirejs.config({
    baseUrl: 'js/lib',
    paths: {
      app: '../app',
      jquery: 'jquery'
    }
});

// Start the main app logic.
requirejs(['jquery', 'grooscript', 'grooscript-binder'], function($) {

    item = {
        text: '',
        area: ''
    };
    console.log('Exists a variable item with a property text in main context.')

    var binder = Binder();
    $(document).ready(function() {
        binder.bind('#text', item, 'text');
        console.log('#text changes are binded to item.text. Now, item has a method setText(newValue) binded to #text.');
        binder.bind('#area', item, 'area');
        console.log('Area binded too.');
    });

    console.log('Textarea:' + $('#area').is("textarea"));
});