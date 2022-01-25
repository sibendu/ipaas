import React , { useState ,useEffect } from 'react';
import ReactDOM from 'react-dom';
import Axios from 'axios'; 
import logo from './logo.svg';
import './style.css';
import Header from './common/header';
import LeftBar from './common/leftbar';
import CustomerAllView from './pages/customer_all_view';
import SingleCustomerView from './pages/customer_single_view';
import Home from './pages/home';
function App() {
    const [page, setPage] = useState("Home");
    const [customerid, setCustomerid] = useState(0);
    useEffect(() => { 
        console.log("App called");
    }, [page]);
    const handleLeftbarLinkClick = (page) => {
        console.log("page2:"+ page);
        setPage(page);
      }
      const handleCustomerViewClick = (page,custid) => {
        console.log("page2:"+ page);
        setPage(page);
        setCustomerid(custid);
      }
  return (
    <div class="wrapper">
    <LeftBar onClick={handleLeftbarLinkClick}/>
    <div id="content">
        <Header/>
        {page==="Home"?<Home/>:page==="CustomerAllView"?<CustomerAllView handleCustomerViewClick={handleCustomerViewClick}/>:
        page==="SingleCustomerView"?<SingleCustomerView Customerid={customerid}/>:<Home/>}
    </div>
</div>
  );
}

export default App;
