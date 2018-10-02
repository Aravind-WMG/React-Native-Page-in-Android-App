import React, { Component } from "react";
import {
  Platform,
  StyleSheet,
  Text,
  View,
  Image,
  Alert,
  TextInput,
  Button,
  FlatList,
  ActivityIndicator
} from "react-native";
import { sampleCarouselData, sampleProductDetail } from "../data/DummyData";
export class PdpComponent extends Component {
  constructor(props) {
    super(props);
    this.state = {
      inputText: "",
      isLoading: true,
      showFetchData: false,
      fetchData: sampleCarouselData,
      datasource: sampleProductDetail
    };
  }
  componentDidMount() {
    return fetch("http://www.mocky.io/v2/5b927cec3300004c0020604f")
      .then(response => response.json())
      .then(responseJson => {
        this.setState(
          {
            isLoading: false,
            datasource: responseJson
          },
          function() {}
        );
      })
      .catch(error => {
        console.error(error);
      });
    // this.setState({ isLoading: false });
  }
  handleClick = () => {
    if (this.state.inputText.length == 0) {
      Alert.alert("Please Fill Gift Card Number");
    } else {
      Alert.alert("Submitted");
    }
  };
  handleData = () => {
    return fetch("http://www.mocky.io/v2/5b927dc33300006600206052")
      .then(response => response.json())
      .then(responseJson => {
        this.setState(
          {
            fetchData: responseJson,
            showFetchData: true
          },
          function() {}
        );
      })
      .catch(error => {
        console.error(error);
      });
    // this.setState({ showFetchData: true });
  };
  dropdownRenderSeparator(sectionID, rowID, adjacentRowHighlighted) {
    let key = rowID;
    return <View style={styles.dropdownSeparator} key={key} />;
  }
  render() {
    if (this.state.isLoading) {
      return (
        <View style={[styles.containerLoader]}>
          <ActivityIndicator size="large" color="#0000ff" />
        </View>
      );
    } else {
      return (
        <View style={styles.container}>
          <View style={styles.imageStyleWrap}>
            <Image
              // source={require("../images/pdt-image.jpg")}
              source={{
                uri: this.state.datasource[0].singlePdtDetail.visualNavTile[0]
                  .imageUrl
              }}
              style={styles.imageStyle}
            />
          </View>
          <View style={styles.priceWrap}>
            <Text style={styles.pdtTitle}>
              {this.state.datasource[0].singlePdtDetail.pdt_name}
            </Text>
            <Text style={styles.salePrice}>
              SALE {this.state.datasource[0].singlePdtDetail.sale_price}
            </Text>
            <Text style={styles.regPrice}>
              Reg {this.state.datasource[0].singlePdtDetail.reg_price}
            </Text>
          </View>
          <View style={styles.inputWrap}>
            <TextInput
              style={styles.textInputStyle}
              placeholder="Type Your Gift Card here..."
              onChangeText={text => this.setState({ inputText: text })}
            />
            <Button
              onPress={this.handleClick}
              title="SUBMIT"
              color="#841584"
              accessibilityLabel="Learn more about this purple button"
            />
          </View>
          <View>
            <Button
              onPress={this.handleData}
              title="CLICK TO GET DATA"
              color="#841584"
              accessibilityLabel="Learn more about this purple button"
            />
            {this.state.showFetchData && (
              <View style={{ flex: 1, paddingTop: 20 }}>
                <FlatList
                  data={this.state.fetchData[0].visualNavTiles.visualNavTile}
                  renderItem={({ item }) => (
                    <Text style={styles.navLabel}>{item.navLabel}</Text>
                  )}
                  keyExtractor={(item, index) => item.navLabel}
                />
              </View>
            )}
          </View>
          <View>
            <Text style={styles.titleStyle}>FlatList Example</Text>
            <FlatList
              data={this.state.datasource[0].youMayLikeThisDetail}
              keyExtractor={(item, index) => index.toString()}
              ItemSeparatorComponent={this.dropdownRenderSeparator.bind(this)}
              renderItem={({ item }) => (
                <View style={styles.flatlistWrap}>
                  <Image
                    // source={require("../images/pdt-image.jpg")}
                    source={{
                      uri: item.imageUrl
                    }}
                    style={styles.imageStyleFlalist}
                  />
                  <Text style={styles.pdtTitle}>{item.pdt_name}</Text>
                </View>
              )}
            />
          </View>
        </View>
      );
    }
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#F5FCFF"
  },
  imageStyleWrap: {
    alignItems: "center",
    paddingVertical: 20
  },
  imageStyle: {
    width: 300,
    height: 300
  },
  imageStyleFlalist: {
    width: 250,
    height: 250
  },
  priceWrap: {},
  pdtTitle: {
    fontSize: 22,
    fontWeight: "bold",
    color: "#000",
    marginLeft: 10
  },
  salePrice: {
    fontSize: 26,
    fontWeight: "bold",
    color: "#000",
    marginTop: 10,
    marginLeft: 10
  },
  regPrice: {
    fontSize: 16,
    fontWeight: "bold",
    color: "#000",
    marginBottom: 5,
    marginLeft: 10
  },
  inputWrap: {
    alignItems: "center",
    marginTop: 20,
    marginBottom: 20
  },
  textInputStyle: {
    fontSize: 16,
    fontWeight: "bold",
    color: "#000",
    width: 300
  },
  dropdownSeparator: {
    height: 1,
    backgroundColor: "#000",
    marginVertical: 20
  },
  titleStyle: {
    fontSize: 22,
    fontWeight: "bold",
    color: "#000",
    marginTop: 10,
    marginLeft: 10,
    marginBottom: 10
  },
  flatlistWrap: {
    flex: 1,
    alignItems: "center"
  },
  containerLoader: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center"
  },
  navLabel: {
    fontSize: 20,
    fontWeight: "bold",
    color: "#000",
    marginLeft: 10,
    marginBottom: 10
  }
});
