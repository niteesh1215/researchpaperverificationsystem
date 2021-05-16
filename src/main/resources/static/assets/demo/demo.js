type = {
  primary: 'primary',
  info: 'info',
  success: 'success',
  warning: 'warning',
  danger: 'danger',
};
utilities = {

  showNotification: function (from, align, errorType, message, durationInMs = 5000) {
    //color = Math.floor((Math.random() * 4) + 1);

    $.notify({
      icon: "tim-icons icon-alert-circle-exc",
      message: message

    }, {
      type: errorType,
      timer: durationInMs,
      placement: {
        from: from,
        align: align
      }
    });
  },

  gradientChartOptionsConfigurationWithTooltipPurple: {
    maintainAspectRatio: false,
    legend: {
      display: false
    },

    tooltips: {
      backgroundColor: '#f5f5f5',
      titleFontColor: '#333',
      bodyFontColor: '#666',
      bodySpacing: 4,
      xPadding: 12,
      mode: "nearest",
      intersect: 0,
      position: "nearest"
    },
    responsive: true,
    scales: {
      yAxes: [{
        barPercentage: 1.6,
        gridLines: {
          drawBorder: false,
          color: 'rgba(29,140,248,0.0)',
          zeroLineColor: "transparent",
        },
        ticks: {
          //suggestedMin: 60,
          //suggestedMax: 125,
          padding: 20,
          fontColor: "#9a9a9a"
        }
      }],

      xAxes: [{
        barPercentage: 1.6,
        gridLines: {
          drawBorder: false,
          color: 'rgba(225,78,202,0.1)',
          zeroLineColor: "transparent",
        },
        ticks: {
          padding: 20,
          fontColor: "#9a9a9a",
          autoSkip: false,
        }
      }]
    }
  },

  gradientChartOptionsConfigurationForPieChart: {
    legend: {
      labels: {
        fontColor: "white",
      }
    },
    maintainAspectRatio: false,
    tooltips: {
      backgroundColor: '#f5f5f5',
      titleFontColor: '#333',
      bodyFontColor: '#666',
      bodySpacing: 4,
      xPadding: 12,
      mode: "nearest",
      intersect: 0,
      position: "nearest"
    },
    responsive: true,
  },

  generatePurpleLineChart: function (id, jsonData) {
    var ctx = document.getElementById(id).getContext("2d");

    var gradientStroke = ctx.createLinearGradient(0, 230, 0, 50);

    gradientStroke.addColorStop(1, 'rgba(72,72,176,0.2)');
    gradientStroke.addColorStop(0.2, 'rgba(72,72,176,0.0)');
    gradientStroke.addColorStop(0, 'rgba(119,52,169,0)'); //purple colors

    var data = {
      labels: Object.keys(jsonData),
      datasets: [{
        label: "Total Submissions",
        fill: true,
        backgroundColor: gradientStroke,
        borderColor: '#d048b6',
        borderWidth: 2,
        borderDash: [],
        borderDashOffset: 0.0,
        pointBackgroundColor: '#d048b6',
        pointBorderColor: 'rgba(255,255,255,0)',
        pointHoverBackgroundColor: '#d048b6',
        pointBorderWidth: 20,
        pointHoverRadius: 4,
        pointHoverBorderWidth: 15,
        pointRadius: 4,
        data: Object.values(jsonData),
      }]
    };

    var myChart = new Chart(ctx, {
      type: 'line',
      data: data,
      options: this.gradientChartOptionsConfigurationWithTooltipPurple,
    });

    return myChart;
  },

  generateOrangeShadesPiechart: function (id, jsonData) {

    var ctx = document.getElementById(id).getContext("2d");

    var data = {
      labels: Object.keys(jsonData),
      datasets: [{
        label: 'My First Dataset',
        data: Object.values(jsonData),
        backgroundColor: [
          'rgba(255,138,118,0.3)',
          'rgba(255,138,118,0.6)',
          'rgba(255,138,118,0.8)',
          'rgba(255,138,118)',

          /*'rgba(31,142,241,0.2)',
          'rgba(31,142,241,0.4)',
          'rgba(31,142,241,0.7)',
          'rgba(31,142,241)',
          'rgba(0,214,180,0.1)',
          'rgba(0,214,180,0.3)',
          'rgba(0,214,180,0.6)',
          'rgba(0,214,180,0.9)', */
        ],
        borderWidth: 0.5,
        hoverOffset: 4
      }]
    };

    return new Chart(ctx, {
      type: 'pie',
      data: data,
      options: this.gradientChartOptionsConfigurationForPieChart
    });

  },
  functions: {
    getTotalVerifiedCount: function (jsonObject) {
      var totalVerified = 0;
      jsonObject.forEach((row) => {
        if (row["verification_status"] === "verified")
          totalVerified++;
      });

      return totalVerified;
    },
    getUniqueFieldCount: function (jsonObject, fieldName) {
      var arr = jsonObject,
        obj = {};
      for (var i = 0; i < arr.length; i++) {
        if (!obj[arr[i][fieldName]]) {
          obj[arr[i][fieldName]] = 1;
        } else if (obj[arr[i][fieldName]]) {
          obj[arr[i][fieldName]] += 1;
        }
      }
      return obj;
    },
    scrollToTop: function () {
      console.log('scrolling to top');
      window.scrollTo({ top: 0, behavior: 'smooth' });
    },

    addCustomModalListener: function (modelOpeningElementSelector, staticWrapper = true) {
      var modal = $('#customModal');

      // Get the button that opens the modal
      var openingElement = $(modelOpeningElementSelector);

      // When the user clicks the button, open the modal 
      openingElement.click(function () {
        modal.css('display', 'block');
      });

      // When the user clicks on <span> (x), close the modal
      modal.on('click','.custom-modal-close', function() {
        modal.css('display', 'none');
      });

      // When the user clicks anywhere outside of the modal, close it
      if (!staticWrapper)
        window.onclick = function (event) {
          if (event.target == modal) {
            modal.style.display = "none";
          }
        }
    },
  },
  api: {
    getColumnMapping: function () {
      return new Promise(function (resolve, reject) {
        $.ajax({
          type: "get",
          contentType: "application/json",
          url: "/columns-mapping",
          timeout: 600000,
          success: function (data) {
            resolve(data);
          },
          error: function (e) {

            console.log(e);
            reject(e);
          }
        });
      });

    },
  },

};


/* dashboard */

//upload menu option
uploadView = {
  init: async function () {

    const actualBtn = document.getElementById('actual-upload-btn');
    const fileChosen = document.getElementById('file-chosen');

    var fileName;
    var jsonData;

    var columnMap;

    try {
      columnMap = await utilities.api.getColumnMapping();
      console.log(columnMap);
    } catch (rejectedValue) {
      utilities.showNotification('top', 'center', type.warning, '<strong>Error!</strong> Please check the internet connection and refresh the page (Server cannot be reached).');
    }
    if (columnMap == undefined)
      return;

    actualBtn.addEventListener('change', function () {

      fileChosen.textContent = this.files[0].name
      convertExcelToJSON(this.files[0]);
    });


    function convertExcelToJSON(file) {
      var allowedExtensions =
        /(\.xls|\.xlsx)$/i;

      console.log(file.name);

      if (!allowedExtensions.exec(file.name)) {

        utilities.showNotification('top', 'center', type.warning, '<strong>Invalid file!</strong> Please choose only valid file type (.xls, .xlsx).')
        actualBtn.value = '';
        fileChosen.textContent = 'No file selected.';
        return false;
      }
      console.log(window.XLSX);

      //XLSX.utils.json_to_sheet(data, 'out.xlsx');

      if (file) {
        let fileReader = new FileReader();
        fileReader.readAsBinaryString(file);
        fileReader.onload = (event) => {
          let data = event.target.result;
          let workbook = XLSX.read(data, {
            type: "binary"
          });
          console.log(workbook);

          sheet = workbook.SheetNames[0];

          var table = XLSX.utils.sheet_to_html(workbook.Sheets[sheet]);
          jsonData = XLSX.utils.sheet_to_json(workbook.Sheets[sheet]);
          fileName = file.name;

          jsonData = sanitizeKeys(jsonData);
          if (checkIfValidColumns(Object.keys(jsonData[0])))
            buildUiFromData(jsonData, table, file.name);
        }
      }
    }

    function sanitizeKeys(jsonData) {
      var keys = Object.keys(jsonData[0])
      for (var i = 0; i < jsonData.length; i++) {
        for (var key of keys) {
          jsonData[i][key.toLowerCase().trim()] = jsonData[i][key];
          delete jsonData[i][key];
        }
      }
      return jsonData;
    }

    function checkIfValidColumns(arrayOfColumns) {
      arrayOfColumns = arrayOfColumns.map(column => column.toLowerCase());
      var unmatchedColumns = [];

      //replace(/\d+.\d+/g,'')
      columnMap.forEach((column) => {
        if (!arrayOfColumns.includes(column.mappedName.toLowerCase()))
          unmatchedColumns.push(column.mappedName);
      });

      if (unmatchedColumns.length == 0)
        return true;
      else {
        utilities.showNotification('top', 'center', type.warning, `<strong>Column not found!</strong> ${unmatchedColumns.toString().replaceAll(',', ', ')} <br/>These columns are not found. Please refer <a href="/modify-column" class="text-info">here</a>.`, 15000);
        return false;
      }
    }

    function buildUiFromData(jsonData, table, tableTitle) {

      var varifiedCount = utilities.functions.getTotalVerifiedCount(jsonData);

      $(".hidden").removeClass('hidden');

      $(".total-count span").html(jsonData.length);
      $(".verified-count span").html(varifiedCount);
      $(".unverified-count span").html(jsonData.length - varifiedCount);

      var departmentMappedName, indexingMapping;

      for (var i = 0; i < columnMap.length; i++) {
        if (columnMap[i].columnName == "department") {
          departmentMappedName = columnMap[i].mappedName.toLowerCase();
        } else if (columnMap[i].columnName == "indexing") {
          indexingMapping = columnMap[i].mappedName.toLowerCase();
        }
        if (departmentMappedName != undefined && indexingMapping != undefined)
          break;
      }

      displayDepartmentWiseCountGrapth(utilities.functions.getUniqueFieldCount(jsonData, departmentMappedName));
      displayIndexWiseCountGraph(utilities.functions.getUniqueFieldCount(jsonData, indexingMapping));

      $(".table-card .card-title").html(tableTitle);
      $("#table-preview").html(table);

      $("#table-preview table").addClass("table table-hover table-bordered ps-child").prepend("<thead class=\"text-primary\"></thead>");
      $("#table-preview table tr").eq(0).appendTo('#table-preview table thead');

      var i = 0;
      $("#table-preview table tr").each(function (index) {
        $(this).prepend("<td>" + i + "</td>");
        i++;
      });

      $("#table-preview table td").css({
        "text-align": "center",
      });


      $("#table-preview table tbody tr").on("mouseenter mouseleave", "td", function () {
        var toolTipText = $("#table-preview table thead tr").children().eq($(this).index()).text();

        if (toolTipText != undefined && toolTipText.length != 0 && $(this).attr('title') == undefined) {
          $(this).addClass("tooltip-test");

          $(this).attr("title", toolTipText);
          //console.log($(this).index());
        }
      });
    }

    function displayDepartmentWiseCountGrapth(jsonDepartmentWiseCountData) {

      var myLineChart = utilities.generatePurpleLineChart("chartLinePurple", jsonDepartmentWiseCountData);

    }


    function displayIndexWiseCountGraph(jsonIndexWiseCoutData) {

      var myPiechart = utilities.generateOrangeShadesPiechart("indexChart", jsonIndexWiseCoutData);

    }

    /* upload button click */
    $('.upload-button').on('click', function () {
      $('#file-upload-form-model').modal({
        backdrop: 'static'
      });

      $('#file-upload-form-model').modal('toggle');

      console.log(fileName)

      $('#file-upload-form input[name=filename]').val(fileName);

      var container = $('.bootstrap-iso form').length > 0 ? $('.bootstrap-iso form').parent() : "body";

      var dateField = $('#file-upload-form input[name=date]');
      //dateField.on('onkeydown',() => false);
      dateField.datepicker({
        format: 'dd/mm/yyyy',
        container: container,
        todayHighlight: true,
        autoclose: true,
      });

      dateField.css({
        "color": "rgba(255, 255, 255, 0.8)",
        "cursor": "default"
      });

    });



    $('#file-upload-form').on('submit', function (e) {

      e.preventDefault();

      console.log("clicked");
      var dateField = $('#file-upload-form input[name=date]');
      if (dateField.val() === '') {
        dateField.addClass('is-invalid');
        return;
      } else {
        dateField.removeClass('is-invalid');
      }

      var fileDetailsForm = $(this).serializeArray().reduce(function (obj, item) {
        obj[item.name] = item.value;
        return obj;
      }, {});

      /*console.log("kaka11");
      console.log(JSON.parse(JSON.stringify(fileDetailsForm)));
      console.log(Object.keys(fileDetailsForm));
      console.log({"filename":fileDetailsForm.filename,"date":fileDetailsForm.date,"description":fileDetailsForm.description});
      console.log(jsonData);*/

      $(this).trigger("reset");

      $('#file-upload-form-model').modal('hide');
      /*for(var i = 0; i <jsonData.length;i++){
        jsonData[i] = JSON.stringify(jsonData[i], (k, v) => v && typeof v === 'object' ? v : '' + v);
      } 
      console.log("stringified json data ");
      console.log(jsonData);
      */
      saveData({
        "fileDetails": fileDetailsForm,
        "excelData": jsonData,
      });

    });

    function saveData(jsonData) {
      $.ajax({
        type: "post",
        contentType: "application/json",
        url: "/save",
        data: JSON.stringify(jsonData),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
          $('.upload-button').addClass('hidden');
          utilities.showNotification('top', 'center', type.success, '<strong>Success!</strong> Uploaded successfuly.');
        },
        error: function (e) {
          utilities.showNotification('top', 'center', type.danger, '<strong>Failed!</strong> Failed to save the details. (' + e.responseJSON.message + ').');
        }
      });
    }

  },

};

//column list view

columnListView = {
  init: async function () {
    try {
      this.buildTable(await utilities.api.getColumnMapping());
    } catch (rejectedValue) {
      utilities.showNotification('top', 'center', type.warning, '<strong>Error!</strong> Please check the internet connection and refresh the page (Server cannot be reached).');
    }
    this.addListeners();
  },

  buildTable: function (jsonData) {
    var tableBody = $('#columnmaptable tbody');
    tableBody.empty();
    jsonData.forEach((obj) => {
      var actionTd = `<td class="text-right">
      <button type="button" data="${obj.id}" rel="tooltip" class="btn btn-success btn-sm btn-round btn-icon">
        <i class="tim-icons icon-settings"></i>
      </button>
      <button type="button" data="${obj.id}" rel="tooltip" class="btn btn-danger btn-sm btn-round btn-icon delete-button" tooltip="Delete">
        <i class="tim-icons icon-trash-simple"></i>
      </button>
    </td>`;
      tableBody.append(`<tr><td class="text-center">${obj.id}</td><td>${obj.columnName}</td><td>${obj.mappedName}</td><td>${obj.dataLength}</td>${actionTd}</tr>`);
    });


    $('.loading-indicator').remove();
  },

  addListeners: function () {
    //add button listener
    $('.add-btn').on('click', function () {
      $('#addColumMappingModel').modal({
        backdrop: 'static'
      });

      $('#addColumMappingModel').modal('toggle');

    });

    //add column form submit listener

    $('#addColumnForm').on('submit', function (e) {

      e.preventDefault();

      var columnDetails = $(this).serializeArray().reduce(function (obj, item) {
        obj[item.name] = item.value;
        return obj;
      }, {});

      $(this).trigger("reset");

      $('#addColumMappingModel').modal('hide');

      $.ajax({
        type: "post",
        contentType: "application/json",
        url: "/add-new-column-mapping",
        data: JSON.stringify(columnDetails),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
          reloadColumnMapping();

        },
        error: function (e) {
          utilities.showNotification('top', 'center', type.danger, '<strong>Failed!</strong> Failed to add the column (' + e.responseJSON.message + ').');
        }
      });

      async function reloadColumnMapping() {
        utilities.showNotification('top', 'center', type.success, '<strong>Success!</strong> Column was added successfuly.');
        try {
          columnListView.buildTable(await utilities.api.getColumnMapping());
        } catch (rejectedValue) {
          utilities.showNotification('top', 'center', type.warning, '<strong>Error!</strong> Please check the internet connection and refresh the page (Server cannot be reached).');
        }
      }

    });

    //deleteButton listener
    $('#columnmaptable tbody').on('click', '.delete-button', function () {
      var id = $(this).attr("data");

      var confirmationModel = $('#confirmationModel');

      confirmationModel.modal({
        backdrop: 'static'
      });

      confirmationModel.modal('show');

      $('#confirmButton').html('DELETE');

      $('#confirmButton').on('click', function (e) {
        confirmationModel.modal('hide');
        $(this).unbind();
        $.ajax({
          type: "get",
          contentType: "application/json",
          url: `/delete-column-mapping/${id}`,
          dataType: 'json',
          cache: false,
          timeout: 600000,
          success: function (data) {
            reloadColumnMapping();
          },
          error: function (e) {
            utilities.showNotification('top', 'center', type.danger, '<strong>Failed!</strong> Failed to delete the column.');
          }
        });

        async function reloadColumnMapping() {
          utilities.showNotification('top', 'center', type.success, '<strong>Success!</strong> Column was deleted successfuly.');
          try {
            columnListView.buildTable(await utilities.api.getColumnMapping());
          } catch (rejectedValue) {
            utilities.showNotification('top', 'center', type.warning, '<strong>Error!</strong> Please check the internet connection and refresh the page (Server cannot be reached).');
          }

        }
      });

      $('#closeModel').on('click', (e) => {
        $('#confirmButton').unbind();
        confirmationModel.modal('hide');
      });

    });
  },
};

//file uploads list view

fileUploadsListView = {
  init: function () {
    this.getFileUploads(this.buildTable);
    this.addEventListeners();
  },
  columnMaps: [],
  selectedFileJsonData: undefined,
  checkedList: [],
  selectedFileName: "",
  selectedFileId: undefined,
  getFileUploads: function (callback) {
    $.ajax({
      type: "get",
      contentType: "application/json",
      url: "/get-uploads",
      timeout: 600000,
      success: function (data) {

        console.log(data);
        callback(data)

      },
      error: function (e) {

        console.log(e);
      }
    });
  },
  buildTable: function (jsonData) {
    var tableBody = $('#fileUploadListTable tbody');
    tableBody.empty();
    var i = 1;
    jsonData.forEach((obj) => {
      var actionTd = `<td class="text-right">
      <button type="button" data-index="${obj.id}" data-filename="${obj.fileName}" rel="tooltip" class="btn btn-warning btn-sm btn-round btn-icon view-button">
        <i class="fas fa-eye"></i>
      </button>
      <button type="button" data="${obj.id}" rel="tooltip" class="btn btn-danger btn-sm btn-round btn-icon delete-button" tooltip="Delete">
        <i class="tim-icons icon-trash-simple"></i>
      </button>
    </td>`;
      tableBody.append(`<tr><td class="text-center">${i}</td><td title="yyyy-mm-dd">${obj.date}</td><td>${obj.fileName}</td><td>${obj.description}</td>${actionTd}</tr>`);
      i++;
    });
    $('.loading-indicator').remove();
  },

  fetchResearchDetailsAndBuildUI: function () {
    $.ajax({
      type: "get",
      contentType: "application/json",
      url: `/get-research-details/${fileUploadsListView.selectedFileId}`,
      dataType: 'json',
      cache: false,
      timeout: 600000,
      success: function (data) {

        fileUploadsListView.selectedFileJsonData = data;
        fileUploadsListView.buildDetailsUi(data);
      },
      error: function (e) {
        utilities.showNotification('top', 'center', type.danger, '<strong>Failed!</strong> Failed to get the details.');
      }
    });
  },

  buildDetailsUi: async function (jsonData) {
    var varifiedCount = utilities.functions.getTotalVerifiedCount(jsonData);
    $("#detailsView").removeClass('hidden');
    $("#tableView").addClass('hidden');
    $(".total-count span").html(jsonData.length);
    $(".verified-count span").html(varifiedCount);
    $(".unverified-count span").html(jsonData.length - varifiedCount);

    var jsonDepartmentWiseCountData = utilities.functions.getUniqueFieldCount(jsonData, 'department');
    var jsonIndexWiseCoutData = utilities.functions.getUniqueFieldCount(jsonData, 'indexing');

    var myLineChart = utilities.generatePurpleLineChart("chartLinePurple", jsonDepartmentWiseCountData);
    var myPiechart = utilities.generateOrangeShadesPiechart("indexChart", jsonIndexWiseCoutData);

    var detailsTableView = $('#detailsTableView');

    $(".table-card .card-title").html(fileUploadsListView.selectedFileName);
    detailsTableView.html(`<div class="text-center loading-indicator"><div class="lds-roller"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div></div>`);

    if (this.columnMaps.length == 0) {
      try {
        this.columnMaps = await utilities.api.getColumnMapping();
      } catch (rejectedValue) {
        utilities.showNotification('top', 'center', type.warning, '<strong>Error!</strong> Please check the internet connection and refresh the page (Server cannot be reached).');
      }
    }
    if (this.columnMaps.length == 0)
      return;

    detailsTableView.empty();
    detailsTableView.append(`<table class=""><tr class="text-primary"><th class="text-center text-primary">#</th><th>Select</th></tr></table>`);
    this.columnMaps.forEach(function (obj) {
      detailsTableView.find('table tr').eq(0).append(`<th>${obj.mappedName}</th>`);
    });
    detailsTableView.find('table tr').eq(0).append(`<th>Verification Status</th>`);


    var i = 1;


    jsonData.forEach((row) => {

      var checkedStatus = fileUploadsListView.checkedList.includes(`${row.id}`) ? "checked" : "";

      var checkbox = /*html*/ `
      <div class="form-check">
      <label class="form-check-label">
        <input class="form-check-input" type="checkbox" value="" ${checkedStatus} data-id="${row.id}" data-index="${i - 1}">
        <span class="form-check-sign">
          <span class="check"></span>
        </span>
      </label>
      </div>`;

      detailsTableView.find('table').append(`<tr><td>${i}</td><td>${checkbox}</td></tr>`);
      this.columnMaps.forEach(function (obj) {
        detailsTableView.find('table tr').eq(i).append(`<td title="${obj.mappedName}">${row[obj.columnName]}</td>`);
      });
      detailsTableView.find('table tr').eq(i).append(`<td title="Verification Status">${row["verification_status"]}</td>`);
      i++;
    });

    detailsTableView.find('table').addClass('table table-hover table-bordered text-center');

  },

  buildForm: function (index) {

    var jsonObj = fileUploadsListView.selectedFileJsonData[index];
    var form = '<form><div class="row">';

    this.columnMaps.forEach(function (obj) {
      var typeSelectFields = ['is_publication_mandatory_phd_requirement', 'is_publication_outcome_of_shodh_pravartan_or_irg', 'is_authorship_affiliated_to_kjc'];


      if (typeSelectFields.includes(obj.columnName)) {
        var options = jsonObj[obj.columnName].trim().toLowerCase() == 'yes' ? `<option selected>yes</option>
        <option>no</option>`: `<option>yes</option>
        <option selected>no</option>`;

        form += `<div class="col-lg-4 col-md-6 col-xl-4 mb-1">
        <div class="form-group">
        <label for="${obj.columnName}">${obj.mappedName}</label>
        <select class="form-control" id="${obj.columnName}">
          ${options}
        </select>
      </div>
      </div>
        `;

      } else {
        var inputType = obj.columnName === 'year' ? 'number' : 'text';

        form += `<div class="col-lg-4 col-md-6 col-xl-4 mb-1"><div class="form-group">
      <label for="${obj.columnName}">${obj.mappedName}</label>
      <input type="${inputType}" class="form-control" value="${jsonObj[obj.columnName]}" id="${obj.columnName}" aria-describedby="${obj.columnName}Help" placeholder="Enter ${obj.mappedName}" required maxlength="${obj.dataLength}">
      <small id="${obj.columnName}Help" class="form-text text-warning">Max ${obj.dataLength} characters allowed.</small>
    </div></div>`;
      }


    });

    form += '<div class="col-12"><button type = "button" class="btn btn-warning custom-modal-close">Cancel</button>';
    form += '<button type="submit" class="btn btn-primary float-right">Update</button></div>';

    form = form + '</div><br><br></form';

    return form;

  },

  addEventListeners: function () {

    //view button listener
    $('#fileUploadListTable tbody').on('click', '.view-button', function () {
      var id = $(this).attr("data-index");
      var filename = $(this).attr("data-filename");
      fileUploadsListView.selectedFileId = id;
      fileUploadsListView.selectedFileName = filename;

      fileUploadsListView.checkedList = [];

      fileUploadsListView.fetchResearchDetailsAndBuildUI();

    });

    //deleteButton listener
    $('#fileUploadListTable tbody').on('click', '.delete-button', function () {
      var id = $(this).attr("data");

      var confirmationModel = $('#confirmationModel');

      confirmationModel.modal({
        backdrop: 'static'
      });

      confirmationModel.modal('show');

      confirmationModel.find('.modal-body').html('Are you sure that you want to delete this entry? <br /><strong>All the data related to the file will also be deleted and this cannot be undone.</strong>');

      $('#confirmButton').html('DELETE');

      $('#confirmButton').on('click', function (e) {
        confirmationModel.modal('hide');
        $(this).unbind();
        $.ajax({
          type: "get",
          contentType: "application/json",
          url: `/delete-upload/${id}`,
          dataType: 'json',
          cache: false,
          timeout: 600000,
          success: function (data) {
            utilities.showNotification('top', 'center', type.success, '<strong>Success!</strong> File was deleted successfuly.');
          },
          error: function (e) {
            console.log(e);
            utilities.showNotification('top', 'center', type.danger, '<strong>Failed!</strong> Failed to delete the file.');
          }
        });

      });

      $('#closeModel').on('click', (e) => {
        $('#confirmButton').unbind();
        confirmationModel.modal('hide');
      });

    });

    //back button click listener
    $('.back-button').on('click', function () {
      $('#tableView').removeClass('hidden');
      $('#detailsView').addClass('hidden');
    });

    //re-verify button click listener

    $('#reVerifyButton').on('click', reverify);

    function reverify() {
      //unbinding the the click event to avoid multiple request to the server
      $(this).unbind();

      $(this).addClass('rotate-animation btn-info');
      utilities.showNotification('top', 'center', type.info, '<strong>Started!</strong> Verification started.');

      $.ajax({
        type: "get",
        contentType: "application/json",
        url: `/verify-file/${fileUploadsListView.selectedFileId}`,
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
          if (data.status == 'success') {
            fileUploadsListView.fetchResearchDetailsAndBuildUI();
            utilities.showNotification('top', 'center', type.success, '<strong>Success!</strong> Verification completed.');
          }

          //adding back the click listener removed previously
          $('#reVerifyButton').on('click', reverify);
          $('#reVerifyButton').removeClass('rotate-animation btn-info');

        },
        error: function (e) {
          console.log(e);
          utilities.showNotification('top', 'center', type.danger, '<strong>Failed!</strong> An error occurred.');

          //adding back the click listener removed previously
          $('#reVerifyButton').on('click', reverify);
          $('#reVerifyButton').removeClass('rotate-animation btn-info');
        }
      });
    }

    //check box listener
    $("#detailsTableView").on('change', 'input[type=checkbox]', (e) => {

      if (this.checkedList.length === 0) {
        $(".action-buttons, .selection-count").removeClass('hidden');
      }
      var id = $(e.target).attr('data-id');
      var index = $(e.target).attr('data-index');

      if (e.target.checked)
        this.checkedList.push({
          'id': id,
          'index': index,
        });
      else
        this.checkedList = this.checkedList.filter(item => item.id !== id);

      if (this.checkedList.length === 0)
        $(".action-buttons, .selection-count").addClass('hidden');
      else
        $(".selection-count").html(`Total <span class="text-danger">${this.checkedList.length}</span> row(s) selected.`);
    });

    //row delete button listener
    $('#deleteRowButton').on('click', (e) => {
      //alert(this.checkedList);

      var confirmationModel = $('#confirmationModel');

      confirmationModel.modal({
        backdrop: 'static'
      });

      confirmationModel.modal('show');

      confirmationModel.find('.modal-body').html('Are you sure that you want to delete the selected rows ? <br /><strong> This cannot be undone.</strong>');

      $('#confirmButton').html('DELETE');

      $('#confirmButton').on('click', function (e) {
        confirmationModel.modal('hide');
        $(this).unbind();
        utilities.showNotification('top', 'center', type.info, '<strong>Delete initiated!</strong> Please wait....', 3000);
        const ids = fileUploadsListView.checkedList.map(obj => obj.id);
        $.ajax({
          type: "post",
          contentType: "application/json",
          url: '/delete-rows',
          dataType: 'json',
          data: JSON.stringify(ids),
          cache: false,
          timeout: 600000,
          success: function (data) {

            /*
              -Hide the selected count displaying element and the action buttons (edit,delete etc.)
              -Empty the checkedList since the fields the are deleted.
              -Rebuild the ui, with updated data
            */
            $(".selection-count,.action-buttons").addClass('hidden');
            fileUploadsListView.checkedList = [];
            fileUploadsListView.fetchResearchDetailsAndBuildUI();
            utilities.showNotification('top', 'center', type.success, '<strong>Success!</strong> Row(s) deleted successfuly.');

          },
          error: function (e) {
            utilities.showNotification('top', 'center', type.danger, '<strong>Failed!</strong> Failed to delete the row(s).');
          }
        });

      });

      $('#closeModel').on('click', (e) => {
        $('#confirmButton').unbind();
        confirmationModel.modal('hide');
      });

    });

    //row edit button listener
    $('#editRowButton').on('click', (e) => {
      $('#displayForm').html(fileUploadsListView.buildForm(fileUploadsListView.checkedList[0].index));
      var formContainer = $('#customModal');
      if (fileUploadsListView.checkedList.length > 1)
        utilities.showNotification('top', 'center', type.info, '<strong>Multiple rows selected!</strong> Choosing the first selected.', 3000);
      formContainer.find('.card-title').html('Update Details');
    });

    utilities.functions.addCustomModalListener('#editRowButton');

    //row verify button listener
    $('#reVerifyRowButton').on('click', function () {

      utilities.showNotification('top', 'center', type.info, '<strong>Reverifying!</strong> Please wait....', 3000);
      const ids = fileUploadsListView.checkedList.map(obj => obj.id);
      $.ajax({
        type: "post",
        contentType: "application/json",
        url: '/verify-rows',
        dataType: 'json',
        data: JSON.stringify(ids),
        cache: false,
        timeout: 600000,
        success: function (data) {
          if (data.status === 'success') {
            fileUploadsListView.fetchResearchDetailsAndBuildUI();
            utilities.showNotification('top', 'center', type.success, '<strong>Success!</strong> Row(s) re-verified successfuly.');

          } else
            utilities.showNotification('top', 'center', type.danger, '<strong>Failed!</strong> Failed to re-verify the row(s).');

        },
        error: function (e) {
          utilities.showNotification('top', 'center', type.danger, '<strong>Failed!</strong> Failed to re-verify the row(s).');
        }
      });

    });

    //row verification info button listener
    $('#rowVerificationInfoButton').on('click', (e) => {
      alert(this.checkedList);
    });

  },




}