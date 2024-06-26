import React, { useState, useEffect } from "react";
import ReactDOM from "react-dom/client";
import { createHashRouter, RouterProvider } from "react-router-dom";
import "./styles/index.css";
import App from "./App.jsx";
import Options from "./Options.jsx";
import Timeline from "./Timeline.jsx";
import AboutUs from "./AboutUs.jsx";
import Toolbar from "./Toolbar.jsx";
import Healthy from "./Healthy.jsx";
import Risky from "./Risky.jsx";
import Dead from "./Dead.jsx";
import Footer from "./Footer.jsx";

// Define handleRouteChange outside the Root component
const handleRouteChange = () => {
  window.scrollTo(0, 0);
  console.log("route change");
};

const Root = () => {
  const [selectedPlant, setSelectedPlant] = useState(null);
  const [timelinePage, setTimelinePage] = useState("");
  const [optionsObj, setOptionsObj] = useState({
    name: "",
    light_level: "",
    preferred_watering_frequency: "",
    soil: "",
    preferred_average_temperature: "",
    preferred_pot_size: "",
    plant_care: "",
    humidity: "",
    soil_ph: "",
  });

  const handleSelectedPlant = (plant) => {
    setSelectedPlant(plant);
  };

  const handleOptionsObject = (
    plant,
    light,
    water,
    soil,
    temp,
    potSize,
    plantCare,
    humidity,
    pH,
  ) => {
    setOptionsObj({
      ...optionsObj,
      name: plant,
      light_level: light,
      preferred_watering_frequency: water,
      soil: soil,
      preferred_average_temperature: temp,
      preferred_pot_size: potSize,
      plant_care: plantCare,
      humidity: humidity,
      soil_ph: pH,
    });
  };

  useEffect(() => {
    // Listen for hashchange event and call handleRouteChange
    window.addEventListener("hashchange", handleRouteChange);

    // Clean up the event listener on component unmount
    return () => {
      window.removeEventListener("hashchange", handleRouteChange);
    };
  }, []); // Empty dependency array to run effect only once

  return (
    <React.StrictMode>
      <Toolbar timelinePage={timelinePage} />
      <RouterProvider
        onUpdate={handleRouteChange}
        router={makeRouter({
          selectedPlant,
          handleSelectedPlant,
          handleOptionsObject,
          optionsObj,
          timelinePage,
          setTimelinePage,
        })}
      />
      <Footer timelinePage={timelinePage} />
    </React.StrictMode>
  );
};

function makeRouter({
  selectedPlant,
  handleSelectedPlant,
  handleOptionsObject,
  optionsObj,
  timelinePage,
  setTimelinePage,
}) {
  return createHashRouter([
    {
      path: "/",
      element: (
        <App
          selectedPlant={selectedPlant}
          onPlantChange={handleSelectedPlant}
        />
      ),
    },
    {
      path: "/options",
      element: (
        <Options
          selectedPlant={selectedPlant}
          handleOptionsObject={handleOptionsObject}
        />
      ),
    },
    {
      path: "/timeline",
      element: (
        <Timeline
          optionsObj={optionsObj}
          timelinePage={timelinePage}
          setTimelinePage={setTimelinePage}
          selectedPlant={selectedPlant}
        />
      ),
    },
    {
      path: "/aboutus",
      element: <AboutUs setTimelinePage={setTimelinePage} />,
    },
    {
      path: "/healthy",
      element: <Healthy />,
    },
    {
      path: "/risky",
      element: <Risky />,
    },
    {
      path: "/dead",
      element: <Dead />,
    },
  ]); // Removed extra parenthesis here
}

ReactDOM.createRoot(document.getElementById("root")).render(<Root />);
