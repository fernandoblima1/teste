import { useState } from 'react';
import DropzoneComponent from 'react-dropzone-component';
import './App.css';

function App() {
  const [count, setCount] = useState(0);
  var componentConfig = {
    iconFiletypes: ['.jpg', '.png', '.gif'],
    showFiletypeIcon: true,
    postUrl: '/uploadHandler',
  };

  var djsConfig = {
    addRemoveLinks: true,
    params: {
      myParameter: "I'm a parameter!",
    },
  };

  function eventHandlers(e) {
    console.log('evento');
  }

  return (
    <>
      <DropzoneComponent
        config={componentConfig}
        eventHandlers={eventHandlers}
        djsConfig={djsConfig}
      />
    </>
  );
}

export default App;
