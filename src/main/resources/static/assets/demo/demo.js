type = {
  primary: 'primary',
  info: 'info',
  success: 'success',
  warning: 'warning',
  danger: 'danger',
};

pieChartShades = {
  orange: 'orange',
  green: 'green',
  blue: 'blue',
  pink: 'pink',
}

var token_csrf, header_csrf;

$(document).ready(function () {
  token_csrf = $("meta[name='_csrf']").attr("content");
  header_csrf = $("meta[name='_csrf_header']").attr("content");
  console.log(header_csrf);
  console.log(token_csrf);
});

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
  gradientBarChartConfiguration: {
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

        gridLines: {
          drawBorder: false,
          color: 'rgba(29,140,248,0.1)',
          zeroLineColor: "transparent",
        },
        ticks: {
          padding: 20,
          fontColor: "#9e9e9e",

        }
      }],

      xAxes: [{

        gridLines: {
          drawBorder: false,
          color: 'rgba(29,140,248,0.1)',
          zeroLineColor: "transparent",
        },
        ticks: {
          padding: 20,
          fontColor: "#9e9e9e",
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
  generateBarChart: function (id, jsonData) {
    var ctx = document.getElementById(id).getContext("2d");

    var gradientStroke = ctx.createLinearGradient(0, 230, 0, 50);

    gradientStroke.addColorStop(1, 'rgba(29,140,248,0.2)');
    gradientStroke.addColorStop(0.4, 'rgba(29,140,248,0.0)');
    gradientStroke.addColorStop(0, 'rgba(29,140,248,0)'); //blue colors


    return new Chart(ctx, {
      type: 'bar',
      responsive: true,
      legend: {
        display: false
      },
      data: {
        labels: Object.keys(jsonData),
        datasets: [{
          label: "Department",
          fill: true,
          backgroundColor: gradientStroke,
          hoverBackgroundColor: gradientStroke,
          borderColor: '#1f8ef1',
          borderWidth: 2,
          borderDash: [],
          borderDashOffset: 0.0,
          data: Object.values(jsonData),
        }]
      },
      options: this.gradientBarChartConfiguration
    });
  },
  generatePieChart: function (id, jsonData, shade = pieChartShades.orange) {

    var ctx = document.getElementById(id).getContext("2d");

    function getOrangeShades() {
      return [
        'rgba(255,138,118,0.3)',
        'rgba(255,138,118,0.6)',
        'rgba(255,138,118,0.8)',
        'rgba(255,138,118)',
      ];
    }

    function getGreenShades() {
      return [
        'rgba(0,214,180,0.3)',
        'rgba(0,214,180,0.6)',
        'rgba(0,214,180,0.8)',
        'rgba(0,214,180)',
      ];
    }

    function getBlueShades() {
      return [
        'rgba(31,142,241,0.3)',
        'rgba(31,142,241,0.6)',
        'rgba(31,142,241,0.8)',
        'rgba(31,142,241)',
      ];
    }

    function getPinkShades() {
      return [
        'rgba(208,72,182,0.2)',
        'rgba(208,72,182,0.5)',
        'rgba(208,72,182,0.7)',
        'rgba(208,72,182)',
      ];
    }

    var backgroundColor = shade == pieChartShades.orange ? getOrangeShades() : shade == pieChartShades.green ? getGreenShades() : shade == pieChartShades.blue ? getBlueShades() : getPinkShades();
    var data = {
      labels: Object.keys(jsonData),
      datasets: [{
        label: 'My First Dataset',
        data: Object.values(jsonData),
        backgroundColor: backgroundColor,
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
    addTableSearchListener: function (inputSelector = '#search', tableSelector = 'table tr') {
      $(inputSelector).keyup(function () {
        var value = this.value.toLowerCase().trim();
        console.log(tableSelector);
        $(tableSelector).each(function (index) {
          if (!index) return;
          $(this).find("td").each(function () {
            var id = $(this).text().toLowerCase().trim();
            var not_found = (id.indexOf(value) == -1);
            $(this).closest('tr').toggle(!not_found);
            return not_found;
          });
        });
      });
    },
  },

  customModal: {
    modal: $('#customModal'),
    openModal: function () {
      utilities.customModal.modal.css('display', 'block');
    },
    closeModal: function () {
      utilities.customModal.modal.css('display', 'none');
    },
    addListeners: function (modelOpeningElementSelector, staticWrapper = true) {

      // Get the button that opens the modal
      var openingElement = $(modelOpeningElementSelector);

      // When the user clicks the button, open the modal 
      openingElement.click(utilities.customModal.openModal);

      // When the user clicks on <span> (x), close the modal
      utilities.customModal.modal.on('click', '.custom-modal-close', utilities.customModal.closeModal);

      // When the user clicks anywhere outside of the modal, close it
      if (!staticWrapper)
        window.onclick = function (event) {
          if (event.target == modal) {
            utilities.customModal.closeModal();
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
            reject(e);
          }
        });
      });

    },
  },

};


//dashboard

dashboardView = {
  init: function () {
    this.getData();
  },
  getData: function () {
    $.ajax({
      type: "get",
      contentType: "application/json",
      url: "/dashboard-details",
      timeout: 600000,
      success: function (data) {
        if (data.status === 'success')
          dashboardView.buildUiFromData(data);
        else
          utilities.showNotification('top', 'center', type.warning, '<strong>Error!</strong> Please check the internet connection and refresh the page (Server cannot be reached).');

      },
      error: function (e) {
        utilities.showNotification('top', 'center', type.warning, '<strong>Error!</strong> Please check the internet connection and refresh the page (Server cannot be reached).');
      }
    });
  },
  buildUiFromData: function (jsonData) {
    $('.hidden').removeClass('hidden');
    $('.loading-indicator').remove();
    $('.total-entries-count span').html(jsonData.total_entries);
    $('.total-uploads-count span').html(jsonData.uploads_count);

    utilities.generatePieChart('verificationStatusChart', dashboardView.getLabelCountPairJSONData(jsonData.verification_status_count_list), pieChartShades.pink);
    utilities.generatePieChart('indexingWiseSubmissionsChart', dashboardView.getLabelCountPairJSONData(jsonData.indexing_count_list), pieChartShades.green);
    dashboardView.displayIndexingWiseVerificationBarChart(jsonData.indexing_wise_verification_status_count);
    utilities.generateBarChart('departmentWiseSubmissionsChart', dashboardView.getLabelCountPairJSONData(jsonData.department_count_list));
    utilities.generatePurpleLineChart('yearWiseSubmissionsChart', dashboardView.getLabelCountPairJSONData(jsonData.year_wise_count));
  },
  getLabelCountPairJSONData: function (jsonArray) {
    var labelCountJSONData = {};
    jsonArray.forEach(function (json) {
      var keys = Object.keys(json);
      var labelKeyName;
      for (var i = 0; i < keys.length; i++) {
        if (keys[i] != 'count') {
          labelKeyName = keys[i];
          break;
        }
      }
      labelCountJSONData[json[labelKeyName]] = json['count'];
    });

    return labelCountJSONData;
  },

  displayIndexingWiseVerificationBarChart: function (jsonArray) {
    var ctx = document.getElementById('indexingWiseverificationStatus').getContext("2d");

    var indexingArray = ['Web of Science', 'Scopus', 'UGC CARE', 'Refereed / Peer Reviewed'];

    function getDatasetTemplate() {
      return {
        label: "",
        fill: true,
        backgroundColor: "",
        borderColor: '',
        borderWidth: 2,
        borderDash: [],
        borderDashOffset: 0.0,
        pointBackgroundColor: '',
        pointBorderColor: 'rgba(255,255,255,0)',
        pointHoverBackgroundColor: '',
        pointBorderWidth: 20,
        pointHoverRadius: 4,
        pointHoverBorderWidth: 15,
        pointRadius: 4,
        data: [],
      };
    }


    var gradientStroke = ctx.createLinearGradient(0, 230, 0, 50);
    gradientStroke.addColorStop(1, 'rgba(72,72,176,0.2)');
    gradientStroke.addColorStop(0.2, 'rgba(72,72,176,0.0)');
    gradientStroke.addColorStop(0, 'rgba(119,52,169,0)'); //purple colors
    var notVerifiedDataset = getDatasetTemplate();
    notVerifiedDataset.label = "not verified";
    notVerifiedDataset.backgroundColor = gradientStroke;
    notVerifiedDataset.borderColor = '#d048b6';
    notVerifiedDataset.pointBackgroundColor = '#d048b6';
    notVerifiedDataset.pointHoverBackgroundColor = '#d048b6';


    //notVerifiedDataset.type ='bar';

    gradientStroke = ctx.createLinearGradient(0, 230, 0, 50);
    gradientStroke.addColorStop(1, 'rgba(29,140,248,0.2)');
    gradientStroke.addColorStop(0.4, 'rgba(29,140,248,0.0)');
    gradientStroke.addColorStop(0, 'rgba(29,140,248,0)'); //blue colors
    var notFoundDataset = getDatasetTemplate();
    notFoundDataset.label = "not found";
    notFoundDataset.backgroundColor = gradientStroke;
    notFoundDataset.borderColor = '#1f8ef1';
    notFoundDataset.pointBackgroundColor = '#1f8ef1';
    notFoundDataset.pointHoverBackgroundColor = '#1f8ef1';
    //notFoundDataset.type='bar';

    gradientStroke = ctx.createLinearGradient(0, 230, 0, 50);
    gradientStroke.addColorStop(1, 'rgba(66,134,121,0.15)');
    gradientStroke.addColorStop(0.4, 'rgba(66,134,121,0.0)'); //green colors
    gradientStroke.addColorStop(0, 'rgba(66,134,121,0)'); //green colors
    var foundDataset = getDatasetTemplate();
    foundDataset.label = "found";
    foundDataset.backgroundColor = gradientStroke;
    foundDataset.borderColor = '#00d6b4';
    foundDataset.pointBackgroundColor = '#00d6b4';
    foundDataset.pointHoverBackgroundColor = '#00d6b4';
    //foundDataset.type = 'bar';

    var notVerifiedArray = [];
    var notFoundArray = [];
    var foundArray = [];
    for (var j = 0; j < indexingArray.length; j++) {
      var verificationStatusJson = {
        'not_verified': 0,
        'not_found': 0,
        'found': 0,
      };
      for (var i = 0; i < jsonArray.length; i++) {
        if (jsonArray[i].indexing == indexingArray[j]) {
          verificationStatusJson[jsonArray[i].verification_status] = jsonArray[i].count;
        }
      }

      notVerifiedArray.push(verificationStatusJson.not_verified);
      notFoundArray.push(verificationStatusJson.not_found);
      foundArray.push(verificationStatusJson.found);
    }

    notVerifiedDataset.data = notVerifiedArray;
    foundDataset.data = foundArray;
    notFoundDataset.data = notFoundArray;


    var data = {
      labels: indexingArray,
      datasets: [notVerifiedDataset, notFoundDataset, foundDataset],
    };

    console.log([notVerifiedDataset, notFoundDataset, foundDataset]);
    console.log([notVerifiedArray, notFoundArray, foundArray]);


    utilities.gradientBarChartConfiguration.legend.display = true;
    var myChart = new Chart(ctx, {
      type: 'bar',
      data: data,
      options: utilities.gradientBarChartConfiguration,
    });

    utilities.gradientBarChartConfiguration.legend.display = false;

  }
};

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

      var myPiechart = utilities.generatePieChart("indexChart", jsonIndexWiseCoutData);

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

    var headers = {};
    headers[header_csrf] = token_csrf;

    function saveData(jsonData) {
      $.ajax({
        type: "post",
        contentType: "application/json",
        url: "/save",
        data: JSON.stringify(jsonData),
        headers: headers,
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

      var headers = {};
      headers[header_csrf] = token_csrf;

      $.ajax({
        type: "post",
        contentType: "application/json",
        headers: headers,
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
    var myPiechart = utilities.generatePieChart("indexChart", jsonIndexWiseCoutData);

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

      var checkedStatus = '';
      for (obj of fileUploadsListView.checkedList) {
        if (obj.id == row.id.toString()) {
          checkedStatus = "checked";
          break;
        }
      }

      var checkbox = `
      <div class="form-check">
      <label class="form-check-label">
        <input class="form-check-input" type="checkbox" ${checkedStatus} data-id="${row.id}" data-index="${i - 1}">
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

  buildForm: function (index = -1) {

    var jsonObj;
    if (index != -1)
      jsonObj = fileUploadsListView.selectedFileJsonData[index];

    var form = '<form><div class="row">';

    this.columnMaps.forEach(function (obj) {
      var typeSelectFields = ['is_publication_mandatory_phd_requirement', 'is_publication_outcome_of_shodh_pravartan_or_irg', 'is_authorship_affiliated_to_kjc'];


      if (typeSelectFields.includes(obj.columnName)) {

        var options = '';
        if (index != -1)
          options = jsonObj[obj.columnName].trim().toLowerCase() == 'yes' ? `<option selected>yes</option>
        <option>no</option>`: `<option>yes</option>
        <option selected>no</option>`;
        else
          options = '<option>yes</option><option>no</option>';

        form += `<div class="col-lg-4 col-md-6 col-xl-4 mb-1">
        <div class="form-group">
        <label for="${obj.columnName}">${obj.mappedName}</label>
        <select class="form-control" id="${obj.columnName}" name="${obj.columnName}">
          ${options}
        </select>
      </div>
      </div>
        `;

      } else if (obj.columnName == 'indexing') {
        var indexings = ['Scopus', 'UGC CARE', 'Web of Science', 'Refereed / Peer Reviewed'];
        var indexingOptions = '';

        indexings.forEach(function (indexing) {

          var selectedIndexing = '';
          if (index != -1)
            selectedIndexing = indexing == jsonObj.indexing ? 'selected' : '';
          indexingOptions += `<option value="${indexing}" ${selectedIndexing}>${indexing}</option>`;
        });

        form += `<div class="col-lg-4 col-md-6 col-xl-4 mb-1">
        <div class="form-group">
        <label for="${obj.columnName}">${obj.mappedName}</label>
        <select class="form-control" id="${obj.columnName}" name="${obj.columnName}">
          ${indexingOptions}
        </select>
      </div>
      </div>
        `;

      } else {
        var inputType = obj.columnName === 'year' ? 'number' : 'text';

        var value = '';
        if (index != -1)
          value = jsonObj[obj.columnName];

        var dateFormatInstructions = ''
        if (obj.columnName == 'date') {
          dateFormatInstructions = ' (dd/mm/yyyy)';
        }

        form += `<div class="col-lg-4 col-md-6 col-xl-4 mb-1"><div class="form-group">
      <label for="${obj.columnName}">${obj.mappedName} ${dateFormatInstructions}</label>
      <input type="${inputType}" class="form-control" name="${obj.columnName}" value="${value}" id="${obj.columnName}" aria-describedby="${obj.columnName}Help" placeholder="Enter ${obj.mappedName}" required maxlength="${obj.dataLength}">
      <small id="${obj.columnName}Help" class="form-text text-warning">Max ${obj.dataLength} characters allowed.</small>
    </div></div>`;
      }
    });

    var verificationOptions = "";
    var verificationStatuses = ["not_verified", "found", "not_found", "partial_match"];

    verificationStatuses.forEach(function (status) {

      var selectedStatus = '';
      if (index != -1)
        selectedStatus = status == jsonObj.verification_status ? 'selected' : '';
      verificationOptions += `<option value="${status}" ${selectedStatus}>${status}</option>`;
    });

    form += `<div class="col-lg-4 col-md-6 col-xl-4 mb-1">
    <div class="form-group">
    <label for="verification_status">Verification Status</label>
    <select class="form-control" id="verification_status" name="verification_status">
      ${verificationOptions}
    </select>
  </div>
  </div>`;

    form += '<div class="col-12"><button type = "button" class="btn btn-warning custom-modal-close">Cancel</button>';
    form += '<button type="submit" class="btn btn-primary float-right">Update</button></div>';

    form = form + '</div><br><br></form';

    return form;

  },

  addEventListeners: function () {

    //search textbox listener
    utilities.functions.addTableSearchListener('#search', '#fileUploadListTable tr');

    $('.selection-count').on('click', '#unselectCheckboxes', function () {
      fileUploadsListView.checkedList = [];
      $('#detailsTableView input:checkbox').prop('checked', false);
      $(".action-buttons, .selection-count").addClass('hidden');
      $('#addNewRow').removeClass('hidden');
    });

    //view button listener
    $('#fileUploadListTable tbody').on('click', '.view-button', function () {
      var id = $(this).attr("data-index");
      var filename = $(this).attr("data-filename");
      fileUploadsListView.selectedFileId = id;
      fileUploadsListView.selectedFileName = filename;

      fileUploadsListView.checkedList = [];

      $('#search').unbind();
      utilities.functions.addTableSearchListener('#search', '#detailsTableView table tr');
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
            fileUploadsListView.getFileUploads(fileUploadsListView.buildTable);
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
      $('#search').unbind();
      utilities.functions.addTableSearchListener('#search', '#fileUploadListTable tr');
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
        $('#addNewRow').addClass('hidden');
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

      if (this.checkedList.length === 0) {
        $(".action-buttons, .selection-count").addClass('hidden');
        $('#addNewRow').removeClass('hidden');
      }
      else
        $(".selection-count").html(`Total <span class="text-danger">${this.checkedList.length}</span> row(s) selected.  <button class="btn btn-warning btn-sm ml-1" id="unselectCheckboxes">UNSELECT ALL</button>`);
    });


    //add new row 

    $('#addNewRow').on('click', function () {

      $('#displayForm').html(fileUploadsListView.buildForm());
      var formContainer = $('#customModal');
      formContainer.find('.card-title').html('Add New Row');

    });

    utilities.customModal.addListeners('#addNewRow');

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

        var headers = {};
        headers[header_csrf] = token_csrf;

        $.ajax({
          type: "post",
          contentType: "application/json",
          headers: headers,
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
            $('#addNewRow').removeClass('hidden');

            fileUploadsListView.checkedList = [];
            fileUploadsListView.fetchResearchDetailsAndBuildUI();
            utilities.showNotification('top', 'center', type.success, '<strong>Success!</strong> Row(s) deleted successfuly.');

          },
          error: function (e) {
            console.log(e);
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

    utilities.customModal.addListeners('#editRowButton');



    //edit row or add new row form
    $('#displayForm').on('submit', 'form', function (e) {
      e.preventDefault();

      utilities.customModal.closeModal();
      var rowDetails = $(this).serializeArray().reduce(function (obj, item) {
        obj[item.name] = item.value;
        return obj;
      }, {});

      var action = '';
      var initializeMessage = '';
      var successMessage = '';
      var failureMessage = '';
      if (fileUploadsListView.checkedList.length > 0) {
        rowDetails['id'] = fileUploadsListView.checkedList[0].id;
        action = '/update-row';
        initializeMessage = 'Updating row!';
        successMessage = 'Row updated';
        failureMessage = 'update';
      }
      else {
        rowDetails['group_id'] = fileUploadsListView.selectedFileId;
        action = '/add-new-row';
        initializeMessage = 'Adding row!';
        successMessage = 'Row added';
        failureMessage = 'add';
      }


      utilities.showNotification('top', 'center', type.info, `<strong>${initializeMessage}</strong> Please wait....`);

      var headers = {};
      headers[header_csrf] = token_csrf;

      $.ajax({
        type: "post",
        contentType: "application/json",
        url: action,
        headers: headers,
        dataType: 'json',
        data: JSON.stringify(rowDetails),
        cache: false,
        timeout: 600000,
        success: function (data) {
          if (data.status === 'success') {
            fileUploadsListView.fetchResearchDetailsAndBuildUI();
            utilities.showNotification('top', 'center', type.success, `<strong>Success!</strong> ${successMessage} successfully.`);
          } else
            utilities.showNotification('top', 'center', type.danger, `<strong>Failed!</strong> Failed to ${failureMessage} the row.`);
        },
        error: function (e) {
          utilities.showNotification('top', 'center', type.danger, `<strong>Failed!</strong> Failed to ${failureMessage} the row.`);
        }
      });
    });

    //row verify button listener
    $('#reVerifyRowButton').on('click', function () {

      utilities.showNotification('top', 'center', type.info, '<strong>Reverifying!</strong> Please wait....', 3000);
      const ids = fileUploadsListView.checkedList.map(obj => obj.id);

      var headers = {};
      headers[header_csrf] = token_csrf;

      $.ajax({
        type: "post",
        contentType: "application/json",
        url: '/verify-rows',
        headers: headers,
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
    $('#rowVerificationInfoButton').on('click', function (e) {
      if (fileUploadsListView.checkedList.length > 1)
        utilities.showNotification('top', 'center', type.info, '<strong>Multiple rows selected!</strong> Choosing the first selected.', 3000);
      var id = fileUploadsListView.checkedList[0].id;
      $.ajax({
        type: "get",
        contentType: "application/json",
        url: `/verification-details/${id}`,
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
          console.log(data);
          showVerificationInfoModel(data);
          //utilities.showNotification('top', 'center', type.success, '<strong>Success!</strong> File was deleted successfuly.');
        },
        error: function (e) {
          console.log(e);
          utilities.showNotification('top', 'center', type.danger, '<strong>Failed!</strong> Failed to retrieve the details.');
        }
      });




    });

  },
}



function showVerificationInfoModel(verificationInfoJson) {
  if (verificationInfoJson.id == null) {
    utilities.showNotification('top', 'center', type.danger, '<strong>Not Found!</strong> Verification information is not available.');
    return;
  }
  var modalBody = '<div class="row"><div class="col-12">';
  /*checking if repository details are available or not by checking if fullName is not null.
    checking only full name because if full name is present then other details will be present.
  */
  if (verificationInfoJson.fullName != null)
    modalBody += `<div class="card">
      <div class="card-header">
          <h2 class="card-category">Details found in the repository</h5>
      </div>
      <div class="card-body">
          <div class="row">
              <div class="col-12 col-lg-6 col-xl-6">
                  Full Name: <span class="text-info"> ${verificationInfoJson.fullName}</span>
              </div>
              <div class="col-12 col-lg-6 col-xl-6">
                  Manuscript Title : <span class="text-info"> ${verificationInfoJson.manuscriptTitle}</span>
              </div>
              <div class="col-12 col-lg-6 col-xl-6">
                  Journal Title : <span class="text-info"> ${verificationInfoJson.journalTitle}</span>
              </div>
              <div class="col-12 col-lg-6 col-xl-6">
                  ISSN : <span class="text-info"> ${verificationInfoJson.issn}</span>
              </div>
              <div class="col-12 col-lg-6 col-xl-6">
                  Volume Number : <span class="text-info"> ${verificationInfoJson.volumeNumber}</span>
              </div>
          </div>
      </div>
    </div>
  </div>`;

  // match info card
  modalBody += `<div class="col-12">
  <div class="card">
      <div class="card-header">
          <h2 class="card-category">Match details</h5>
      </div>
      <div class="card-body">
          <div class="row">`;

  var matchStatusSpan = verificationInfoJson.fullNameMatch ? `<span class="text-success"> Matched</span>` : `<span class="text-danger"> Not Matched</span>`;

  modalBody += `<div class="col-12 col-lg-6 col-xl-6">
     Full Name Match: ${matchStatusSpan}
 </div>`;

  matchStatusSpan = verificationInfoJson.manuscriptTitleMatch ? `<span class="text-success"> Matched</span>` : `<span class="text-danger"> Not Matched</span>`;

  modalBody += `<div class="col-12 col-lg-6 col-xl-6">
    Manuscript Title Match: ${matchStatusSpan}
  </div>`;

  matchStatusSpan = verificationInfoJson.journalTitleMatch ? `<span class="text-success"> Matched</span>` : `<span class="text-danger"> Not Matched</span>`;

  modalBody += `<div class="col-12 col-lg-6 col-xl-6">
   Journal Title Match: ${matchStatusSpan}
  </div>`;

  matchStatusSpan = verificationInfoJson.issnMatch ? `<span class="text-success"> Matched</span>` : `<span class="text-danger"> Not Matched</span>`;

  modalBody += `<div class="col-12 col-lg-6 col-xl-6">
  ISSN Match: ${matchStatusSpan}
  </div>`;

  matchStatusSpan = verificationInfoJson.volumeNumberMatch ? `<span class="text-success"> Matched</span>` : `<span class="text-danger"> Not Matched</span>`;

  modalBody += `<div class="col-12 col-lg-6 col-xl-6">
  Volume Number Match: ${matchStatusSpan}
  </div>`;

  modalBody += '</div></div></div></div>';

  //found url card

  if ((verificationInfoJson.foundAtUrls != null) && (verificationInfoJson.foundAtUrls.length != 0)) {
    modalBody += `<div class="col-12">
    <div class="card">
        <div class="card-header">
            <h2 class="card-category">Found Url</h5>
        </div>
        <div class="card-body">`;
    var i = 1;
    verificationInfoJson.foundAtUrls.forEach(function (url) {
      modalBody += `<a href="${url}"><button class="btn btn-warning animation-on-hover" type="button">Visit Url ${i++}</button></a>`
    });

    modalBody += ` </div>
      </div>
    </div>
  </div>`;
  }
  modalBody += '</div>';

  console.log(modalBody);

  $('#verificationInfoModal .modal-body').html(modalBody);

  var confirmationModal = $('#verificationInfoModal');

  confirmationModal.modal({
    backdrop: 'static'
  });

  confirmationModal.modal('show');
}