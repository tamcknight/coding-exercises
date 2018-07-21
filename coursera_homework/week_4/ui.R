library(shiny)
shinyUI(fluidPage(
  titlePanel("Plot Random Numbers"),
  sidebarLayout(
    sidebarPanel(
     
      sliderInput("years", "Select the range of years to plot", sep="",
                  1959, 1997, value = c(1994, 1997)),
     
      checkboxInput("show_xlab", "Show/Hide X Axis Label", value = TRUE),
      checkboxInput("show_ylab", "Show/Hide Y Axis Label", value = TRUE),
      checkboxInput("show_title", "Show/Hide Title", value = TRUE)
    ),
    mainPanel(
      h3("Graph of CO2 data and Forecast for next 12 months"),
      h6("Use the slider to select the years by which you would like to forecast to work from.  The 
         graph will take the input and recalcuate the forecast based on the date range selected.  The
         data will be forecasted out 12 months using the forecast library.  It will use the best model
         available to fit to the line.  The forecast is in blue. Expand the years to see more trend."),
      plotOutput("plot1")
    )
  )
))