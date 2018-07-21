library(shiny)
library(forecast)
shinyServer(function(input, output) {
  output$plot1 <- renderPlot({
    minYear <- input$years[1]
    maxYear <- input$years[2]

    data.subset <- window(co2,start = c(minYear,1), end = c(maxYear,12))
    xlab <- ifelse(input$show_xlab, "Year", "")
    ylab <- ifelse(input$show_ylab, "CO2 Concentration", "")
    main <- ifelse(input$show_title, "Mauna Loa Atmospheric CO2 Concentration", "")
    plot(forecast(data.subset, 12), xlab = xlab, ylab = ylab, main = main, type = 'l')
    
  })
})