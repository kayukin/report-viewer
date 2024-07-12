import {ReactEventHandler} from "react";

interface IFrameProps {
    src: string,
    onLoad: ReactEventHandler<HTMLIFrameElement>
}

export const IFrame = ({src, onLoad}: IFrameProps) => {

    return (
        <>
            <iframe key={src}
                    src={src}
                    onLoad={onLoad}
            >
            </iframe>
        </>
    );
};